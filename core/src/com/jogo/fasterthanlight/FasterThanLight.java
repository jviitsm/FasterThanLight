package com.jogo.fasterthanlight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FasterThanLight extends ApplicationAdapter {

    private Texture carro,fundo,segundoFundo;
	private ShapeRenderer shape;
    private SpriteBatch batch;


    //Atributos
    private float larguraDispositivo,alturaDispositivo;
    private float movimentoDoFundo,movimentoDoSegundoFundo;
    private float posicaoVerticalCarroPrincipal;
    private float posicaoHorizontalCarroPrincipal;
    private float deltaTime;
    private int estadoDoJogo;

    //Câmera
    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH = 800;
    private final float VIRTUAL_HEIGHT = 600;




	@Override
	public void create () {

        carro = new Texture("carroteste.png");
        fundo = new Texture("fundo.jpg");
        segundoFundo = new Texture("fundo.jpg");

        shape = new ShapeRenderer();
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH /2,VIRTUAL_HEIGHT /2,0);
        viewport = new StretchViewport(VIRTUAL_WIDTH,VIRTUAL_HEIGHT,camera);

        /*larguraDispositivo = Gdx.graphics.getWidth();
        alturaDispositivo = Gdx.graphics.getHeight();
        */
        movimentoDoFundo = 0;
        larguraDispositivo = VIRTUAL_WIDTH;
        alturaDispositivo = VIRTUAL_HEIGHT;

        posicaoHorizontalCarroPrincipal = larguraDispositivo /2 + carro.getWidth() /2;
        posicaoVerticalCarroPrincipal =   0;
        estadoDoJogo = 0;

	}

	@Override
	public void render () {

        camera.update();
        deltaTime = Gdx.graphics.getDeltaTime();


        movimentoDoFundo -= deltaTime * 100;
        movimentoDoSegundoFundo -= deltaTime *100;


       //Fundo infinito
        if(movimentoDoFundo <- alturaDispositivo){
            movimentoDoFundo = alturaDispositivo;
        }
        if(movimentoDoSegundoFundo < - alturaDispositivo - alturaDispositivo){
            movimentoDoSegundoFundo = movimentoDoFundo;
        }



        //Impossibilita o carro de sair da tela
        if(posicaoVerticalCarroPrincipal <= 0){
            posicaoVerticalCarroPrincipal =0;
        }
        if(posicaoVerticalCarroPrincipal >= 600 - carro.getHeight()  ){
            posicaoVerticalCarroPrincipal = 600 - carro.getHeight() ;
        }
        posicaoVerticalCarroPrincipal += deltaTime * 300;




        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(fundo,0,movimentoDoFundo,larguraDispositivo,alturaDispositivo);
        batch.draw(fundo,0,movimentoDoSegundoFundo + alturaDispositivo,larguraDispositivo,alturaDispositivo);
        batch.draw(carro,posicaoHorizontalCarroPrincipal,posicaoVerticalCarroPrincipal);


        batch.end();


	}
	
	@Override
	public void dispose () {

	}
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

}
