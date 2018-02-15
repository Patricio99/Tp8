package com.example.a42252248.tp8;

import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCSize;

/**
 * Created by 42252248 on 15/2/2018.
 */

public class clsJuego {
    CCGLSurfaceView _VistaDelJuego;
    CCSize DeviceDisplay;
    Sprite AutoJugador;
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

    }
    class CapaDelFrente extends Layer {
        public CapaDelFrente(){
            PonerAutoPosInicial();
        }
        private void PonerAutoPosInicial(){
            AutoJugador = Sprite.sprite("AutoJugador.png");

            AutoJugador.setPosition(100, 300);

            super.addChild(AutoJugador);
        }

    }

}
