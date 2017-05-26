package com.jogo.fasterthanlight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.text.DecimalFormat;
import java.util.Random;

public class FasterThanLight extends ApplicationAdapter {

    private Texture carro,fundo,segundoFundo,carroInimigo, obstaculoBicho,gameOver;
	private ShapeRenderer shape;
    public static SpriteBatch batch;
    private Texture btl,btr,btu,btd;
    private Boolean apertouCima = false,apertouBaixo = false;
    private Rectangle retanguloCarroInimigo,retanguloCarroPrincipal,retanguloBicho;
    private DecimalFormat df = new DecimalFormat("0");

    //Atributos
    private float larguraDispositivo,alturaDispositivo;
    private float movimentoDoFundo,movimentoDoSegundoFundo;
    private float posicaoVerticalCarroPrincipal;
    private float posicaoHorizontalCarroPrincipal;
    private float posicaoHorizontalBicho,posicaoVerticalBicho;
    private float deltaTime;
    private int estadoDoJogo;
    private float pontuacao;
    private BitmapFont pontos;
    private Random numeroRandom;
    private float posicaoVerticalCarroInimigo, posicaoHorizontalCarroInimigo;
    private Sound bateu,grito;
    private Music musica;
    private boolean bateuSom,bateuSomBicho;



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
        btd = new Texture("flatDark26.png");
        btu = new Texture("flatDark25.png");
        btl = new Texture("flatDark23.png");
        btr = new Texture("flatDark24.png");
        gameOver = new Texture("over.png");
        carroInimigo = new Texture("carroPrincipal.png");
        obstaculoBicho = new Texture("obstaculo1.png");

        numeroRandom = new Random();

        pontos = new BitmapFont();
        pontos.setColor(Color.WHITE);
        pontos.getData().setScale(5);

        shape = new ShapeRenderer();
        batch = new SpriteBatch();

        bateu = Gdx.audio.newSound(Gdx.files.internal("carcrash.mp3"));
        grito = Gdx.audio.newSound(Gdx.files.internal("grito.mp3"));
        retanguloCarroInimigo = new Rectangle();
        retanguloCarroPrincipal = new Rectangle();
        retanguloBicho = new Rectangle();

        musica = Gdx.audio.newMusic(Gdx.files.internal("musica.mp3"));
        musica.setLooping(true);
        musica.setVolume(0.5f);
        musica.play();
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
        pontuacao =0;
        posicaoVerticalCarroInimigo = numeroRandom.nextInt(2000) + 800 + carroInimigo.getHeight();
        posicaoHorizontalCarroInimigo = numeroRandom.nextInt(500) + carroInimigo.getWidth();
        posicaoVerticalBicho = numeroRandom.nextInt(1500) + 900;
        posicaoHorizontalBicho = numeroRandom.nextInt(500) + 80;

// 170,550,
	}

	@Override
	public void render () {

        camera.update();
        shape.setProjectionMatrix(batch.getProjectionMatrix());

        deltaTime = Gdx.graphics.getDeltaTime();


        if(estadoDoJogo ==0){
            if(Gdx.input.justTouched()){
                estadoDoJogo =1;
            }
        }


        if(estadoDoJogo ==1) {

            if(pontuacao < 30){
                movimentoDoFundo -= deltaTime * 300;
                movimentoDoSegundoFundo -= deltaTime * 300;
                posicaoVerticalCarroInimigo -= deltaTime * 300;
                posicaoVerticalBicho -= deltaTime * 300;
                pontuacao += deltaTime * 1;
            }
            else if(pontuacao > 30 && pontuacao < 60){
                movimentoDoFundo -= deltaTime * 450;
                movimentoDoSegundoFundo -= deltaTime * 450;
                posicaoVerticalCarroInimigo -= deltaTime * 450;
                posicaoVerticalBicho -= deltaTime * 450;
                pontuacao += deltaTime * 2;
            }
            else if(pontuacao > 60){
                movimentoDoFundo -= deltaTime * 800;
                movimentoDoSegundoFundo -= deltaTime * 800;
                posicaoVerticalCarroInimigo -= deltaTime * 800;
                posicaoVerticalBicho -= deltaTime * 800;
                pontuacao += deltaTime * 4;
            }

            //Fundo infinito
            if (movimentoDoFundo < -alturaDispositivo) {
                movimentoDoFundo = alturaDispositivo;
            }
            if (movimentoDoSegundoFundo < -alturaDispositivo - alturaDispositivo) {
                movimentoDoSegundoFundo = movimentoDoFundo;
            }


            //Impossibilita o carro de sair da tela
            if (posicaoVerticalCarroPrincipal <= 0) {
                posicaoVerticalCarroPrincipal = 0;
            }
            if (posicaoVerticalCarroPrincipal >= 600 - carro.getHeight()) {
                posicaoVerticalCarroPrincipal = 600 - carro.getHeight();
            }
            if (posicaoHorizontalCarroPrincipal <= 255 - carro.getWidth()) {
                posicaoHorizontalCarroPrincipal = 255 - carro.getWidth();
            }
            if (posicaoHorizontalCarroPrincipal >= 600 - carro.getWidth()) {
                posicaoHorizontalCarroPrincipal = 600 - carro.getWidth();
            }


            //Botões para se mover
            Rectangle bounds = new Rectangle(60, alturaDispositivo / 2, 90, 90);
            Rectangle boundsDown = new Rectangle(680, alturaDispositivo / 2, 90, 90);
            Rectangle boundsLeft = new Rectangle(60, 30, 90, 90);
            Rectangle boundsRight = new Rectangle(680, 30, 90, 90);

            Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            camera.unproject(tmp);
            if (bounds.contains(tmp.x, tmp.y)) {
                posicaoVerticalCarroPrincipal += 2;
                apertouCima = true;
                apertouBaixo = false;
            }
            if (boundsDown.contains(tmp.x, tmp.y)) {
                posicaoVerticalCarroPrincipal -= 2;
                apertouBaixo = true;
                apertouCima = false;
            }
            if (boundsRight.contains(tmp.x, tmp.y)) {
                if (apertouCima == true) {
                    posicaoHorizontalCarroPrincipal += 2;
                    posicaoVerticalCarroPrincipal += 1;
                    apertouCima = true;
                    apertouBaixo = false;
                } else if (apertouBaixo = true) {
                    posicaoHorizontalCarroPrincipal += 2;
                    posicaoVerticalCarroPrincipal -= 1;
                    apertouBaixo = true;
                    apertouCima = false;
                } else {
                    posicaoHorizontalCarroPrincipal += 2;

                }
            }
            if (boundsLeft.contains(tmp.x, tmp.y)) {
                if (apertouCima == true) {
                    posicaoHorizontalCarroPrincipal -= 2;
                    posicaoVerticalCarroPrincipal += 1;
                    apertouCima = true;
                    apertouBaixo = false;
                } else if (apertouBaixo == true) {
                    posicaoHorizontalCarroPrincipal -= 2;
                    posicaoVerticalCarroPrincipal -= 1;
                    apertouBaixo = true;
                    apertouCima = false;
                } else{
                    posicaoHorizontalCarroPrincipal -= 2;
                }

            }




        //Posições do  inimigo


        if(posicaoVerticalCarroInimigo < -alturaDispositivo - carroInimigo.getHeight()){
            posicaoVerticalCarroInimigo = 800 + numeroRandom.nextInt(800) + carroInimigo.getHeight();
            posicaoHorizontalCarroInimigo = 255 + numeroRandom.nextInt(340);
        }
        if(posicaoVerticalBicho < -alturaDispositivo - 100){
            posicaoVerticalBicho = 900 + numeroRandom.nextInt(800);
            posicaoHorizontalBicho = 180 + numeroRandom.nextInt(375);
        }

            if(posicaoHorizontalCarroInimigo > 600){
                posicaoHorizontalCarroInimigo = 500 + numeroRandom.nextInt(50);
            }
            if (posicaoHorizontalCarroInimigo < 255 ){
                posicaoHorizontalCarroInimigo = 255 + carroInimigo.getWidth();
            }
        if(posicaoHorizontalBicho < 180){
            posicaoHorizontalBicho = 180;
        }
        if(posicaoHorizontalBicho > 555){
            posicaoHorizontalBicho = 555;
        }
        }else if(estadoDoJogo ==2){

            if(Gdx.input.justTouched()){
                estadoDoJogo =1;
                posicaoVerticalCarroInimigo = 800 + numeroRandom.nextInt(2000) + carroInimigo.getHeight();
                posicaoHorizontalCarroInimigo = 255 + numeroRandom.nextInt(300) -carro.getWidth();
                posicaoHorizontalCarroPrincipal = larguraDispositivo /2 + carro.getWidth() /2;
                posicaoVerticalBicho = 800 + numeroRandom.nextInt(1000) + obstaculoBicho.getHeight();
                posicaoHorizontalBicho = 170 + numeroRandom.nextInt(370);
                pontuacao =0;
                posicaoVerticalCarroPrincipal =   0;
                bateuSom = false;
                bateuSomBicho = false;

            }
        }

        //Desenha na tela
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(fundo,0,movimentoDoFundo,larguraDispositivo,alturaDispositivo);
        batch.draw(fundo,0,movimentoDoSegundoFundo + alturaDispositivo,larguraDispositivo,alturaDispositivo);
        batch.draw(carroInimigo,posicaoHorizontalCarroInimigo,posicaoVerticalCarroInimigo,60,100);
        batch.draw(obstaculoBicho,posicaoHorizontalBicho,posicaoVerticalBicho,55,55);
        batch.draw(carro,posicaoHorizontalCarroPrincipal,posicaoVerticalCarroPrincipal);
        pontos.draw(batch,String.valueOf(df.format(pontuacao)),larguraDispositivo - 130,alturaDispositivo - 30);
        batch.draw(btd,680,alturaDispositivo /2,90,90);
        batch.draw(btu,60,alturaDispositivo /2,90,90);
        batch.draw(btl,60,30,90,90);
        batch.draw(btr,680,30,90,90);
        if(estadoDoJogo == 2){
            batch.draw(gameOver,250,300 /2,350,350);
        }


        batch.end();

        shape.setProjectionMatrix(batch.getProjectionMatrix());

        retanguloCarroPrincipal = new Rectangle(
                posicaoHorizontalCarroPrincipal,
                posicaoVerticalCarroPrincipal,
                carro.getWidth(),
                carro.getHeight()
        );
        retanguloCarroInimigo = new Rectangle(
                posicaoHorizontalCarroInimigo,
                posicaoVerticalCarroInimigo,
                60,
                100
        );
        retanguloBicho = new Rectangle(
                posicaoHorizontalBicho + 9,
                posicaoVerticalBicho,
                38,
                50
        );

/*
        shape.begin(ShapeRenderer.ShapeType.Filled);

        shape.rect(retanguloCarroPrincipal.x,retanguloCarroPrincipal.y,retanguloCarroPrincipal.width,
                retanguloCarroPrincipal.height);
       shape.rect(retanguloCarroInimigo.x,retanguloCarroInimigo.y,retanguloCarroInimigo.width
               ,retanguloCarroInimigo.height);
        shape.rect(retanguloBicho.x,retanguloBicho.y,retanguloBicho.width,retanguloBicho.height);
        shape.setColor(Color.RED);

*/

        shape.end();

        if(Intersector.overlaps(retanguloCarroPrincipal,retanguloCarroInimigo)){
            estadoDoJogo = 2;
            if (bateuSom == false){
                bateu.play();
                bateuSom = true;
            }
        }
	    if(Intersector.overlaps(retanguloBicho,retanguloCarroInimigo)){
            posicaoHorizontalBicho += 30;
            posicaoVerticalCarroInimigo -= 30;
        }
        if(Intersector.overlaps(retanguloCarroPrincipal,retanguloBicho)){
            estadoDoJogo =2;
            if(bateuSomBicho == false){
                grito.play();
                bateuSomBicho = true;
            }
        }


	}


	
	@Override
	public void dispose () {

	}
    public void resize(int width, int height) {
        viewport.update(width,height);

   }


}
