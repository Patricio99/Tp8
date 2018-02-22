package com.example.a42252248.tp8;

import android.bluetooth.BluetoothClass;
import android.util.Log;
import android.view.MotionEvent;

import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.particlesystem.ParticleSpiral;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;
import org.w3c.dom.Comment;

import java.io.Console;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class clsJuego {
    CCGLSurfaceView _VistaDelJuego;
    CCSize DeviceDisplay;
    Sprite BackgroundImage;
    public clsJuego(CCGLSurfaceView VistaDelJuego){
        _VistaDelJuego = VistaDelJuego;
    }

    public void ComenzarJuego(){
        Director.sharedDirector().attachInView(_VistaDelJuego);

        DeviceDisplay = Director.sharedDirector().displaySize();

        Director.sharedDirector().runWithScene(EscenaDelJuego());
    }



    private Scene EscenaDelJuego(){
        Scene EscenaADevolver;
        EscenaADevolver = Scene.node();

        CapaDeFondo MiCapaFondo;
        MiCapaFondo = new CapaDeFondo();

        CapaDelFrente MiCapaFrente;
        MiCapaFrente = new CapaDelFrente();


        EscenaADevolver.addChild(MiCapaFondo, -10);
        EscenaADevolver.addChild(MiCapaFrente, 10);

        return EscenaADevolver;
    }
    class CapaDeFondo extends Layer{
        public CapaDeFondo(){
            PonerBackground();
        }
        private void PonerBackground(){
            BackgroundImage = Sprite.sprite("background.png");
            BackgroundImage.setPosition(DeviceDisplay.width/2, DeviceDisplay.height/2);

            Float BkgWidth, BkgHeight;
            BkgWidth = DeviceDisplay.width /  BackgroundImage.getWidth();
            BkgHeight = DeviceDisplay.height / BackgroundImage.getHeight();

            BackgroundImage.runAction(ScaleBy.action(0.01f, BkgWidth, BkgHeight));
            super.addChild(BackgroundImage);
        }

    }

    class CapaDelFrente extends Layer {
        private Player player;
        private ArrayList<AutoEnemigo> autoEnemigosLst = new ArrayList<>();
        private boolean userLoose = false;
        private Label lblScore;
        private Label lblLooseMessage;
        @Override
        public boolean ccTouchesMoved(MotionEvent e){

            if ( this.userLoose) {
                ResetGame();
            } else {
                this.player.sprite.setPosition(e.getX(), this.player.sprite.getHeight() / 2 + 50);
            }


            return true;
        }
        @Override
        public boolean ccTouchesEnded(MotionEvent event){


            return true;
        }

        public CapaDelFrente(){
            player = new Player(this);
            super.schedule("AddEnemy", 3.0f);
            super.schedule("Update", 0);
            super.setIsTouchEnabled(true);
        }

        public class Player {
            private Sprite sprite;
            private float x;
            private float y;
            public int points;

            private Player(Layer l) {
                int initialCarril = 2;
                this.sprite = Sprite.sprite("AutoJugador.png");
                this.y = 0;
                this.x = DeviceDisplay.width / 3 * initialCarril + this.sprite.getWidth() / 2;

                this.sprite.setPosition(this.x, this.y);
                l.addChild(this.sprite);
            }
        }
        public class AutoEnemigo {
            private Sprite sprite;
            private float x;
            private float y;
            private float speed;
            private org.cocos2d.actions.base.Action animation;

            private AutoEnemigo(Layer l, int spriteNumber, float speed, int carril) {
                this.sprite = Sprite.sprite("AutoEnemigo" + spriteNumber + ".png");
                this.speed = speed;
                this.x = DeviceDisplay.width / 3 * carril + this.sprite.getWidth() / 2;
                this.y = DeviceDisplay.height + this.sprite.getHeight() / 2;

                this.sprite.setPosition(this.x, this.y);
                l.addChild(this.sprite);
            }
            private void startMoving(){
                this.animation = this.sprite.runAction(MoveTo.action(this.speed, this.x, -1 * (DeviceDisplay.height + this.sprite.getHeight())));
            }
        }
        public void AddEnemy(float timeDiff) {
            super.unschedule("AddEnemy");

            int carril = 0 + (int)(Math.random() * ((2- 0) + 1));
            int spriteNumber = 1 + (int)(Math.random() * ((2- 1) + 1));
            float speed = getSpeed(this.player);
            AutoEnemigo autoEnemigo = new AutoEnemigo(this, spriteNumber, speed, carril);
            autoEnemigo.startMoving();

            autoEnemigosLst.add(autoEnemigo);

            removeFinishedAutoEnemigo(this.autoEnemigosLst);

            // Log.d("APP", "AddEnemy_" + this.autoEnemigosLst.size() + "_" + speed);
            if (!this.userLoose) {
                float nextCar = getNextEnemyTimeElapse(this.player);

                super.schedule("AddEnemy", nextCar );
            } else {
                // this doesn't work
                // Director.sharedDirector().stopAnimation();
                showLooseMessage();
            }
        }
        public void Update(float timeDiff) {
            // Log.d("APP", "UPDATE_" + this.userLoose);
            for (AutoEnemigo element : autoEnemigosLst) {
                if(InterseccionEntreSprites(element.sprite, player.sprite)) {
                    this.userLoose = true;
                    break;
                }
            }
        }



        boolean InterseccionEntreSprites (Sprite Sprite1, Sprite Sprite2) {
            boolean Response;
            Response = false;
            int Sprite1Izquierda, Sprite1Derecha, Sprite1Abajo, Sprite1Arriba;
            int Sprite2Izquierda, Sprite2Derecha, Sprite2Abajo, Sprite2Arriba;
            Sprite1Izquierda=(int) (Sprite1.getPositionX() - Sprite1.getWidth()/2);
            Sprite1Derecha=(int) (Sprite1.getPositionX() + Sprite1.getWidth()/2);
            Sprite1Abajo=(int) (Sprite1.getPositionY() - Sprite1.getHeight()/2);
            Sprite1Arriba=(int) (Sprite1.getPositionY() + Sprite1.getHeight()/2);
            Sprite2Izquierda=(int) (Sprite2.getPositionX() - Sprite2.getWidth()/2);
            Sprite2Derecha=(int) (Sprite2.getPositionX() + Sprite2.getWidth()/2);
            Sprite2Abajo=(int) (Sprite2.getPositionY() - Sprite2.getHeight()/2);
            Sprite2Arriba=(int) (Sprite2.getPositionY() + Sprite2.getHeight()/2);
            Log.d("Interseccion", "Sp1 - Izq: "+Sprite1Izquierda+" - Der: "+Sprite1Derecha+" - Aba:"
                    +Sprite1Abajo+" - Arr: "+Sprite1Arriba);
            Log.d(";Interseccion", "Sp2 - Izq: "+Sprite2Izquierda+" - Der: " +Sprite2Derecha+" - Aba:"
                    +Sprite2Abajo+" - Arr:" +Sprite2Arriba);
//Borde izq y borde inf de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) &&
                    EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {

                Response=true;
            }
//Borde izq y borde sup de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) &&
                    EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {

                Response=true;
            }
//Borde der y borde sup de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {

                Response=true;
            }
//Borde der y borde inf de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {

                Response=true;
            }
//Borde izq y borde inf de Sprite 2 está dentro de Sprite 1
            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {

                Response=true;
            }
//Borde izq y borde sup de Sprite 1 está dentro de Sprite 1
            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {

                Response=true;
            }
//Borde der y borde sup de Sprite 2 está dentro de Sprite 1
            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {

                Response=true;
            }
//Borde der y borde inf de Sprite 2 está dentro de Sprite 1
            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {

                Response=true;
            }
            return Response;
        }

        boolean EstaEntre (int NumeroAComparar, int NumeroMenor, int NumeroMayor){
            boolean Devolver;

            if(NumeroMenor > NumeroMayor) {
                int auxiliar;
                auxiliar = NumeroMayor;
                NumeroMayor = NumeroMenor;
                NumeroMenor = auxiliar;
            }
            if (NumeroAComparar >= NumeroMenor && NumeroAComparar <= NumeroMayor){
                Devolver = true;
            }
            else{
                Devolver = false;
            }
            return  Devolver;

        }








        public float getSpeed(Player p) {
            float speed = - 25 / 10 * p.points + 5;
            if (speed < 1) {
                speed = 1;
            }
            //TODO REMOVE THIS LINE
            speed = 5;
            return speed;
        }
        public float getNextEnemyTimeElapse(Player p) {
            float speed = - 25 / 2 * p.points + 3;
            if (speed < 1) {
                speed = 1;
            }
            return speed;
        }
        public void removeFinishedAutoEnemigo(List<AutoEnemigo> autoEnemigoLst) {
            Iterator<AutoEnemigo> iter = autoEnemigoLst.iterator();

            while (iter.hasNext()) {
                AutoEnemigo autoEnemigo = iter.next();

                if (autoEnemigo.animation.isDone()) {
                    iter.remove();
                    this.player.points++;
                    showUserScore();
                }
            }
        }

        private void ResetGame() {
            this.userLoose = false;
            this.player.points = 0;
            super.schedule("AddEnemy", 3.0f);
            super.removeChild(lblScore, false);
            super.removeChild(lblLooseMessage, false);
        }
        private void showUserScore() {
            super.removeChild(lblScore, false);
            lblScore = Label.label("Tu puntaje es: " + this.player.points, "Verdana", 45);
            lblScore.setPosition(lblScore.getWidth() / 2, DeviceDisplay.getHeight() - lblScore.getHeight() / 2);
            this.addChild(lblScore);
        }
        private void showLooseMessage() {
            super.removeChild(lblLooseMessage, false);
            lblLooseMessage = Label.label("Perdiste!!!", "Verdana", 45);
            lblLooseMessage.setPosition(DeviceDisplay.getWidth() / 2, DeviceDisplay.getHeight() / 2);
            this.addChild(lblLooseMessage);
        }
    }
}

