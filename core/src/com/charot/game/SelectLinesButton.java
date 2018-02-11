package com.charot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Iterator;

/**
 * Created by Sasakin M on 11.02.2018.
 */

public class SelectLinesButton  extends Button {
    public static final float WIDTH = 160;
    public static final float HEIGHT = 60;

    // начальные координаты поумолчанию
    float x1 = 130;
    float y1 = 260;
    float x2 = 250;
    float y2 = 260;

    private Array<Drum> drums;
    private Array<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>> pointArray;
    private Array<Vector2> vectorArray; // массив вектаров, необходимых для построения линии

    public static final Drawable drawableDown = new Image(new Texture("button-1.png")).getDrawable();
    public static final Drawable drawableUp = new Image(new Texture("button-2.png")).getDrawable();

    public SelectLinesButton(final Array<Drum> drums) {
        super(drawableUp, drawableDown);
        this.drums = drums;
        setSize(WIDTH, HEIGHT);
        pointArray = new Array<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>>();
        vectorArray = new Array<Vector2>();
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    if(drums == null || drums.size != 5)
                        return;

                    if(drums.get(4).isRunnung())  // если барабан вращается или не был запущен - то непоказываем линию
                        return;

                    vectorArray.clear();
                    pointArray.clear();
                    findFirst();
                    while(vectorArray.size<5) {
                        ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object1 = pointArray.get(pointArray.size - 1);
                        Vector2 v3 = getNextPoint(object1, drums.get(vectorArray.size));
                        if (v3 != null) {
                            vectorArray.add(v3);
                        } else {
                            vectorArray.add(getNearestPoint(object1, drums.get((vectorArray.size))));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Метод отвечает за подбор координат для построения линии от клетки первого барабана до клетки второго барабана.
     */
    void findFirst() {
        Drum drum1 = drums.get(0);
        Drum drum2 = drums.get(1);
        Iterator<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>> iter1 = drum1.getDrumCells().iterator();
        while (iter1.hasNext()) {
            ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object1 = iter1.next();
            Vector2 secondPoint = getNextPoint(object1, drum2);
            if(secondPoint!=null){
                vectorArray.add(new Vector2(object1.key.x+50,object1.key.y+55));
                vectorArray.add(secondPoint);
                return;
            }
        }
        if(vectorArray.size==0){
            vectorArray.add(new Vector2(x1,y1), new Vector2(x2,y2));
            Iterator<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>> iter2 = drum2.getDrumCells().iterator();
            iter2.next();
            pointArray.add(iter2.next());
        }

    }

    /**
     * Функция ищет в барабане драм объект с такой же текстурой как и у object 1 и возвращает
     * вектор, указывающий на этот объект
     * @param object1
     * @param drum
     * @return
     */
    private Vector2 getNextPoint(ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object1, Drum drum) {
        Iterator<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>> iter2 = drum.getDrumCells().iterator();
        while (iter2.hasNext()) {
            ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object2 = iter2.next();
            if(object1!=null) {
                if (object1.value.first().equals(object2.value.first())) {
                    Vector2 secondPoint = new Vector2(object2.key.x + 50, object2.key.y + 55);
                    pointArray.add(object2);
                    return secondPoint;
                }
            }
        }
        return null;
    }

    /**
     * Функция ищет в барабане drum блюжайший объект 'клетку' по отношению к объекту object1
     * @param object1
     * @param drum
     * @return
     */
    private  Vector2 getNearestPoint(ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object1, Drum drum) {
        Iterator<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>> iter2 = drum.getDrumCells().iterator();
        ArrayMap<Float, Vector2> vectorMap = new ArrayMap<Float, Vector2>();
        Array<Pair<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>,Vector2>> objectMap = new Array<Pair<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>,Vector2>>();
        while (iter2.hasNext()) {
            ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> object2 = iter2.next();
            Vector2 firstPoint = new Vector2(object1.key.x+50,object1.key.y+55);
            Vector2 secondPoint = new Vector2(object2.key.x+50,object2.key.y+55);
            vectorMap.put(firstPoint.dst(secondPoint),secondPoint);
            objectMap.add(new Pair<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>, Vector2>(object2,secondPoint));
        }
        Iterator<ObjectMap.Entry<Float, Vector2>> iter3 = vectorMap.iterator();
        float minDst = 100000000f;
        while (iter3.hasNext()) {
            ObjectMap.Entry<Float, Vector2> object3 = iter3.next();
            if(object3.key<minDst)
                minDst = object3.key;
        }
        Vector2 nearestVector = vectorMap.get(minDst);
        Iterator<Pair<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>,Vector2>> iter4 = objectMap.iterator();
        ObjectMap.Entry<Rectangle, Pair<Integer, Texture>> obj = null;
        while (iter4.hasNext()) {
            Pair<ObjectMap.Entry<Rectangle, Pair<Integer, Texture>>,Vector2> object4 = iter4.next();
            if(object4.second().equals(nearestVector))
                obj = object4.first();
        }

        pointArray.add(obj);
        return nearestVector;
    }

    public static class drawer{
        static ShapeRenderer drawer = new ShapeRenderer();
        public static void line(Vector2 start, Vector2 end, int lineWidth, Color color){
            Gdx.gl.glLineWidth(lineWidth);
            drawer.begin(ShapeRenderer.ShapeType.Line);
            drawer.setColor(color);
            drawer.line(start,end);
            drawer.end();
        }
    }

    public void render() {
        if(vectorArray.size>=5){
            Vector2 v1 = vectorArray.get(0);
            Vector2 v2 = vectorArray.get(1);
            Vector2 v3 = vectorArray.get(2);
            Vector2 v4 = vectorArray.get(3);
            Vector2 v5 = vectorArray.get(4);
            drawer.line(v1, v2, (int) v1.dst(v2), Color.CYAN);
            drawer.line(v2, v3, (int) v2.dst(v3), Color.CYAN);
            drawer.line(v3, v4, (int) v3.dst(v4), Color.CYAN);
            drawer.line(v4, v5, (int) v4.dst(v5), Color.CYAN);
        }
    }
}
