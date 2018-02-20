package com.example.a42252248.tp8;

import android.bluetooth.BluetoothClass;
import android.util.Log;
import android.view.MotionEvent;

import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.particlesystem.ParticleSpiral;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;
import org.w3c.dom.Comment;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class clsJuego {
    CCGLSurfaceView _VistaDelJuego;
    CCSize DeviceDisplay;
    Sprite AutoJugador;
    Sprite AutoEnemigo;
    Sprite AutoEnemigo1;
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


        public CapaDelFrente(){
            player = new Player(this);
            // this.setPosition(0, 0);
            super.schedule("AddEnemy", 3.0f);


        }

        public class Player {
            private Sprite sprite;
            private float x;
            private float y;
            public int points;

            this.setIsTouchEnabled(true);
            @Override
            public boolean ccTouchesBegan(MotionEvent event){

                MoverNaveJugador(event.getX(), event.getY());

                return true;
            }
            void MoverNaveJugador(float DestinoX, float DestinoY){
                float MovimientoHorizontal, Softer;
                MovimientoHorizontal = DestinoX - DeviceDisplay.getWidth()/2;

                Softer = 20;
                MovimientoHorizontal = MovimientoHorizontal/Softer;

                float PosFX;
                PosFX = AutoJugador.getPositionX() + MovimientoHorizontal;

                if (PosFX < AutoJugador.getWidth()/2){
                    PosFX = AutoJugador.getWidth()/2;
                }
                if (PosFX > DeviceDisplay.getWidth() - AutoJugador.getWidth()/2){
                    PosFX = DeviceDisplay.getWidth()-AutoJugador.getWidth()/2;
                }

                AutoJugador.setPosition(PosFX, AutoJugador.getPositionY());
            }
            @Override
            public boolean ccTouchesMoved(MotionEvent event){
                return true;
            }
            @Override
            public boolean ccTouchesEnded(MotionEvent event){
                return true;
            }

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

            int carril = 0 + (int)(Math.random() * ((2- 0) + 1));
            int spriteNumber = 1 + (int)(Math.random() * ((2- 1) + 1));
            float speed = getSpeed(this.player);
            AutoEnemigo autoEnemigo = new AutoEnemigo(this, spriteNumber, speed, carril);
            autoEnemigo.startMoving();

            autoEnemigosLst.add(autoEnemigo);

            removeFinishedAutoEnemigo(this.autoEnemigosLst);
            Log.d("APP", "AddEnemy_" + this.autoEnemigosLst.size() + "_" + speed);
            if (!this.userLoose) {
                float nextCar = getNextEnemyTimeElapse(this.player);
                super.schedule("AddEnemy", nextCar );
            }
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
            for (AutoEnemigo element : autoEnemigoLst) {
                if (element.animation.isDone()) {
                    autoEnemigoLst.remove(autoEnemigoLst.indexOf(element));
                    this.player.points++;
                }
            }
        }

    }
}

