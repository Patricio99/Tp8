package com.example.a42252248.tp8;

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

/**
 * Created by 42252248 on 15/2/2018.
 */

public class clsJuego {
    CCGLSurfaceView _VistaDelJuego;
    CCSize DeviceDisplay;
    Sprite AutoJugador;
    Sprite AutoEnemigo;
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
        public CapaDelFrente(){
            PonerAutoPosInicial();
            PonerAutoEnemigo();
        }
        private void PonerAutoPosInicial(){
            AutoJugador = Sprite.sprite("AutoJugador.png");

            float PosIX, PosIY;
            PosIX = DeviceDisplay.width/2;
            PosIY = AutoJugador.getHeight()/2;
            AutoJugador.setPosition(PosIX, PosIY);

            super.addChild(AutoJugador);
        }
        private void PonerAutoEnemigo(){
            AutoEnemigo = Sprite.sprite("AutoEnemigo.png");

            Float PosIX, PosIY;
            Float EnemyHeight;
            EnemyHeight = AutoEnemigo.getHeight();
            PosIY = DeviceDisplay.height + EnemyHeight/2;
            PosIX = DeviceDisplay.width/2;


            AutoEnemigo.setPosition(PosIX, PosIY);

            Float PosFX, PosFY;
            PosFX = PosIX;
            PosFY = -EnemyHeight/2;

            AutoEnemigo.runAction(MoveTo.action(3, PosFX, PosFY));



            super.addChild(AutoEnemigo);

        }

    }

}
