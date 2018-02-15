package com.example.a42252248.tp8;

import org.cocos2d.nodes.Director;
import org.cocos2d.opengl.CCGLSurfaceView;

/**
 * Created by 42252248 on 15/2/2018.
 */

public class clsJuego {
    CCGLSurfaceView _VistaDelJuego;
    public clsJuego(CCGLSurfaceView VistaDelJuego){
        _VistaDelJuego = VistaDelJuego;
    }
    public void ComenzarJuego(){
        Director.sharedDirector().attachInView(_VistaDelJuego);
    }

}
