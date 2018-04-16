package com.xita.testeflappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TesteFlappy extends ApplicationAdapter
{
    private int estadoDoJogo =0;

    private BitmapFont fonte;
    private int tamanhoDaFonte=6;
    private int pontuacao=0;

    private float distanciaEntreCanos = 11.0f;
    private float variacaoDeCriacaoDeCanos = 200.0f;//inferior baseada no posicao do superior-- essa é do superior
    private float metadeDaTelaProporcao = 1.65f;
    private float metadeDaTelaReal;

    private Random geradorDeRandomico;
    private Random geradorDeRandomicoCurto;
    private int numeroRandomicoA=0;
    private int numeroRandomicoB=0;
    private int numeroRandomicoC=0;
    private int numeroRandomicoD=0;
    private int numeroRandomicoE=0;
    private int numeroRandParaBool=0;
    private boolean positivoOuNagativo=true;

    private Music som;

    private SpriteBatch batch;
    private Texture[] passaro;
    private Texture fundo;
    private Texture canoSuperior;
    private Texture canoInferior;
    private Texture gameOver;

    private float ScreenSizeX=0.0f;
	private float ScreenSizeY=0.0f;

	private float tamanhoPassaroX = 0.0f;
	private float tamanhoPAssaroY = 0.0f;

    private float movimento =0.0f;
    private float velocidade = 0.0f;
    private float valsalto =20.0f;
    private float salto = valsalto;
    private float porcentagem = 0.05f;
	private float porcentagemx = 1.4f;
	private float multiplicador = 0.5f;
	private float posicaoX = 0.0f;//passaro
    private float posicaoXCanoSA = 0.0f;//canos
    private float pocicaoYCanoSA = 0.0f;
    private float posicaoXCanoIA = 0.0f;//canos
    private float pocicaoYCanoIA = 0.0f;
    private float posicaoXCanoSB = 0.0f;//canos
    private float pocicaoYCanoSB = 0.0f;
    private float posicaoXCanoIB = 0.0f;//canos
    private float pocicaoYCanoIB = 0.0f;
    private float posicaoXCanoSC = 0.0f;//canos
    private float pocicaoYCanoSC = 0.0f;
    private float posicaoXCanoIC = 0.0f;//canos
    private float pocicaoYCanoIC = 0.0f;
    private float posicaoXCanoSD = 0.0f;//canos
    private float pocicaoYCanoSD = 0.0f;
    private float posicaoXCanoID = 0.0f;//canos
    private float pocicaoYCanoID = 0.0f;
    private float posicaoXCanoSE = 0.0f;//canos
    private float pocicaoYCanoSE = 0.0f;
    private float posicaoXCanoIE = 0.0f;//canos
    private float pocicaoYCanoIE = 0.0f;

    private float canoNaPosicaoInicial=0.0f;

    private float alturaDoCanoS = 0.0f;
    private float larguraDoCanoS = 0.0f;
    private float alturaDoCanoI = 0.0f;
    private float larguraDoCanoI = 0.0f;

    private float alturaRealDoCanoS;

    private float distanciaEntreProximoCano;
    private float posicaoCanoAnstesDeEntrarNaTela;

    private float ratioParaCanos;

    private float velocidadeDoCano = 1.0f;
    private float deltaTime=6.0f;


	private float variacao=0.0f;

	private int X1,X2,X3;

	private boolean tocouTela = false;
    private boolean tocoufundoTela = false;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final float VIRTUAL_WIDTH = 1080;
    private final float VIRTUAL_HEIGHT = 1920;
	
	@Override
	public void create ()
    {

        som = Gdx.audio.newMusic(Gdx.files.getFileHandle("audio/wing2.mp3", Files.FileType.Internal));

        definindoConfiguracoesDoJogo();
	}

	@Override
	public void render ()
    {
     camera.update();
     limpaTela();

     batch.setProjectionMatrix(camera.combined);
	 batch.begin();

	 if(estadoDoJogo==0)
     {
         //resetaPassaro();
         pontuacao=0;
         movimentaFundo();
         desenhaPontuacao();
         animaPassaro();
         resetaPassaro();
         resetaCanos();
         //desenhaPassaro();

         aguradandoToqueParaIniciar();
     }
     else if(estadoDoJogo==1)
     {
         checandoSeHouveToqueNaTela();
         movimentaFundo();
         //fundoEstatico();
         animaCanos();

         desenhaPontuacao();

         animaPassaro();

         desenhaPassaro();

         checaPassaroXCanoPontos();
         checaPassaroXCanoColisao();

         checandoSeTocouNoFundo();
         calculaGravidadeOuSalto();
     }
     else if(estadoDoJogo==2)
     {
         //definindoConfiguracoesDoJogo();
         aguradandoToqueParaIniciar();
         fundoPausado();
         canosParados();
         desenhaGameOver();
         desenhaPontuacao();
         //animaPassaro();

         desenhaPassaro();
         checandoSeTocouNoFundo();
         calculaGravidadeOuSalto();
     }


     batch.end();
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		//passaro.dispose();
        som.dispose();
	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    public void definindoConfiguracoesDoJogo()
    {
        //metadeDaTelaReal = (float) (ScreenSizeY/ScreenSizeY/1.65);

        fonte = new BitmapFont();
        fonte.setColor(255,255,255,255);
        fonte.getData().setScale(tamanhoDaFonte);

        batch = new SpriteBatch();

        gameOver = new Texture("game_over.png");

        passaro = new Texture[3];
        passaro[0]=new Texture("passaro1.png");
        passaro[1]=new Texture("passaro2.png");
        passaro[2]=new Texture("passaro3.png");

        canoSuperior = new Texture("cano_topo.png");
        canoInferior = new Texture("cano_baixo.png");

        alturaRealDoCanoS = canoSuperior.getHeight();

        larguraDoCanoS = porcentagemx*canoSuperior.getWidth();
        alturaDoCanoS = (porcentagemx*canoSuperior.getHeight())+300;
        larguraDoCanoI = porcentagemx*canoSuperior.getWidth();
        alturaDoCanoI = (porcentagemx*canoSuperior.getHeight())+300;

        distanciaEntreProximoCano = (porcentagemx*canoSuperior.getWidth())*3;


        fundo = new Texture("fundo.png");

        //ScreenSizeX=Gdx.graphics.getWidth();
        //ScreenSizeY=Gdx.graphics.getHeight();

        ScreenSizeX = VIRTUAL_WIDTH;
        ScreenSizeY = VIRTUAL_HEIGHT;

        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT/2,0);

        viewport = new FitViewport(VIRTUAL_WIDTH,VIRTUAL_HEIGHT,camera);



        posicaoCanoAnstesDeEntrarNaTela = (ScreenSizeX+distanciaEntreProximoCano);

        canoNaPosicaoInicial = ScreenSizeX+(porcentagemx*canoSuperior.getWidth());

        ratioParaCanos = alturaRealDoCanoS+((ScreenSizeY*porcentagem)*distanciaEntreCanos);

        metadeDaTelaReal = (float) (ScreenSizeY/metadeDaTelaProporcao);

        movimento = ScreenSizeY/2;
        posicaoX = ScreenSizeY/2;

        posicaoX = ((ScreenSizeX/2-passaro[(int) variacao].getWidth())*multiplicador);

        X1=0;
        X2 = ((int) ScreenSizeX-2);
        X3 = (((int)ScreenSizeX-2)+((int)ScreenSizeX-2));


        tamanhoPassaroX=((ScreenSizeY*porcentagem)*porcentagemx);
        tamanhoPAssaroY=(ScreenSizeY*porcentagem);

        numeroRandomicoA = geraNumAleatorio();
        numeroRandomicoB = geraNumAleatorio();
        numeroRandomicoC = geraNumAleatorio();
        numeroRandomicoD = geraNumAleatorio();
        numeroRandomicoE = geraNumAleatorio();

        posicaoXCanoSA = ScreenSizeX+(porcentagemx*canoSuperior.getWidth());
        posicaoXCanoIA = ScreenSizeX+(porcentagemx*canoInferior.getWidth());

        posicaoXCanoSB = (posicaoXCanoSA+ distanciaEntreProximoCano);
        posicaoXCanoIB = (posicaoXCanoSA+distanciaEntreProximoCano);

        posicaoXCanoSC = (posicaoXCanoSB+distanciaEntreProximoCano);
        posicaoXCanoIC = (posicaoXCanoSB+distanciaEntreProximoCano);

        posicaoXCanoSD =(posicaoXCanoSC+distanciaEntreProximoCano);
        posicaoXCanoID =(posicaoXCanoSC+distanciaEntreProximoCano);

        posicaoXCanoSE =(posicaoXCanoSD+distanciaEntreProximoCano);
        posicaoXCanoIE =(posicaoXCanoSD+distanciaEntreProximoCano);


    }
	public void checandoSeHouveToqueNaTela()
	{
        if(Gdx.input.justTouched())
        {
            try
            {
                tocaSom("baterAsa");
            }
            catch(Exception e) {}

            tocouTela =true;
        }
	}
    public void aguradandoToqueParaIniciar()
    {
        if(Gdx.input.justTouched())
        {
            if(estadoDoJogo==0)
            {
                estadoDoJogo=1;
            }
            else if(estadoDoJogo==2&&tocoufundoTela)
            {
                estadoDoJogo=0;
            }
        }
    }
    public void resetaGravidade()
    {
        velocidade = 0.0f;
    }
	public void calculaGravidadeOuSalto()
	{
	    if(tocouTela==false)
        {
        	if(tocoufundoTela==false)
			{
				movimento=movimento - velocidade;
				//velocidade = velocidade*1.05f;
				velocidade=velocidade+0.5f;
			}

        }
        if(tocouTela==true)
        {
        	velocidade=0.0f;
            movimento=movimento + salto;
            //velocidade = velocidade*1.05f;
            salto=salto-1;
            //Gdx.app.log("salto",""+salto);
            if(salto<1)
            {
                tocouTela=false;
				salto = valsalto;
            }
        }

	}
	public void movimentaFundo()
	{

		batch.draw(fundo,X1,0,ScreenSizeX+1,ScreenSizeY);
        batch.draw(fundo,X2,0,ScreenSizeX+1,ScreenSizeY);
		batch.draw(fundo,X3,0,ScreenSizeX+1,ScreenSizeY);
        X1--;
        if(X3==2)
        {
            X1 = (int) (ScreenSizeX);
        }
        X2--;
        if(X1==2)
        {
            X2 = (int) (ScreenSizeX);
        }
		X3--;
		if(X2==2)
		{
			X3 = (int) (ScreenSizeX);
		}
	}
	public void fundoEstatico()
    {
        batch.draw(fundo,0,0,ScreenSizeX,ScreenSizeY);
    }
    public void fundoPausado()
    {

        batch.draw(fundo,X1,0,ScreenSizeX+1,ScreenSizeY);
        batch.draw(fundo,X2,0,ScreenSizeX+1,ScreenSizeY);
        batch.draw(fundo,X3,0,ScreenSizeX+1,ScreenSizeY);

        if(X3==2)
        {
            X1 = (int) (ScreenSizeX);
        }

        if(X1==2)
        {
            X2 = (int) (ScreenSizeX);
        }

        if(X2==2)
        {
            X3 = (int) (ScreenSizeX);
        }
    }
	public void checandoSeTocouNoFundo()
    {
        if(movimento<5)
        {
            if(estadoDoJogo==1)
            {
                estadoDoJogo =2;
            }

            tocoufundoTela=true;
        }
        if(movimento>4)
        {
            tocoufundoTela=false;
        }
    }
    public void desenhaGameOver()
    {
        batch.draw(gameOver,((ScreenSizeX/2)-(gameOver.getWidth())),(ScreenSizeY/2),gameOver.getWidth()*2,gameOver.getHeight()*2);
    }
    public void desenhaPontuacao()
    {
        fonte.draw(batch,String.valueOf(pontuacao),ScreenSizeX/2,(ScreenSizeY-(ScreenSizeY/8)));
    }
    public void resetaPassaro()
    {
        resetaGravidade();
        movimento = ScreenSizeY/2;
        desenhaPassaro();
    }
    public void desenhaPassaro()
    {
        //define localestatico do bird em X
        batch.draw(passaro[(int) variacao],posicaoX,movimento,tamanhoPassaroX,tamanhoPAssaroY);

    }
    public void desenhaCanoA()
    {
        pocicaoYCanoSA = (float) ((metadeDaTelaReal)+numeroRandomicoA);
        batch.draw(canoSuperior,(posicaoXCanoSA),(pocicaoYCanoSA),(larguraDoCanoS),(alturaDoCanoS));
        pocicaoYCanoIA = pocicaoYCanoSA-ratioParaCanos;
        batch.draw(canoInferior,(posicaoXCanoIA),(pocicaoYCanoIA),(larguraDoCanoI),(alturaDoCanoI));
    }
    public void desenhaCanoB()
    {
        pocicaoYCanoSB = (float) ((metadeDaTelaReal)+numeroRandomicoB);
        batch.draw(canoSuperior,(posicaoXCanoSB),(pocicaoYCanoSB),(larguraDoCanoS),(alturaDoCanoS));
        pocicaoYCanoIB = pocicaoYCanoSB-ratioParaCanos;
        batch.draw(canoInferior,(posicaoXCanoSB),(pocicaoYCanoIB),(larguraDoCanoI),(alturaDoCanoI));
    }
    public void desenhaCanoC()
    {
        pocicaoYCanoSC = (float) ((metadeDaTelaReal)+numeroRandomicoC);
        batch.draw(canoSuperior,(posicaoXCanoSC),(pocicaoYCanoSC),(larguraDoCanoS),(alturaDoCanoS));
        pocicaoYCanoIC = pocicaoYCanoSC-ratioParaCanos;
        batch.draw(canoInferior,(posicaoXCanoSC),(pocicaoYCanoIC),(larguraDoCanoI),(alturaDoCanoI));
    }
    public void desenhaCanoD()
    {
        pocicaoYCanoSD = (float) ((metadeDaTelaReal)+numeroRandomicoD);
        batch.draw(canoSuperior,(posicaoXCanoSD),(pocicaoYCanoSD),(larguraDoCanoS),(alturaDoCanoS));
        pocicaoYCanoID = pocicaoYCanoSD-ratioParaCanos;
        batch.draw(canoInferior,(posicaoXCanoSD),(pocicaoYCanoID),(larguraDoCanoI),(alturaDoCanoI));
    }
    public void desenhaCanoE()
    {
        pocicaoYCanoSE = (float) ((metadeDaTelaReal)+numeroRandomicoE);
        batch.draw(canoSuperior,(posicaoXCanoSE),(pocicaoYCanoSE),(larguraDoCanoS),(alturaDoCanoS));
        pocicaoYCanoIE = pocicaoYCanoSE-ratioParaCanos;
        batch.draw(canoInferior,(posicaoXCanoSE),(pocicaoYCanoIE),(larguraDoCanoI),(alturaDoCanoI));
    }
    public void animaCanos()
    {
        definindoVelocidadeDoCano();
        //se necessario usar == e velocidadedocano3;
        if(posicaoXCanoSE<canoNaPosicaoInicial)
        {
            //geraNumAleatorio();
            posicaoXCanoSA = (posicaoXCanoSE+distanciaEntreProximoCano);
            posicaoXCanoIA = (posicaoXCanoSE+distanciaEntreProximoCano);
        }
        if(posicaoXCanoSA<canoNaPosicaoInicial)
        {
            //geraNumAleatorio();
            posicaoXCanoSB = (posicaoXCanoSA+distanciaEntreProximoCano);
            posicaoXCanoIB = (posicaoXCanoSA+distanciaEntreProximoCano);
        }
        if(posicaoXCanoSB<canoNaPosicaoInicial)
        {
            //geraNumAleatorio();
            posicaoXCanoSC = (posicaoXCanoSB+distanciaEntreProximoCano);
            posicaoXCanoIC = (posicaoXCanoSB+distanciaEntreProximoCano);
        }
        if(posicaoXCanoSC<canoNaPosicaoInicial)
        {
            //geraNumAleatorio();
            posicaoXCanoSD = (posicaoXCanoSC+distanciaEntreProximoCano);
            posicaoXCanoID = (posicaoXCanoSC+distanciaEntreProximoCano);
        }
        if(posicaoXCanoSD<canoNaPosicaoInicial)
        {
            //geraNumAleatorio();
            posicaoXCanoSE = (posicaoXCanoSD+distanciaEntreProximoCano);
            posicaoXCanoIE = (posicaoXCanoSD+distanciaEntreProximoCano);
        }
        posicaoXCanoIA -=velocidadeDoCano;
        posicaoXCanoSA -=velocidadeDoCano;
        posicaoXCanoIB -=velocidadeDoCano;
        posicaoXCanoSB -=velocidadeDoCano;
        posicaoXCanoIC -=velocidadeDoCano;
        posicaoXCanoSC -=velocidadeDoCano;
        posicaoXCanoID -=velocidadeDoCano;
        posicaoXCanoSD -=velocidadeDoCano;
        posicaoXCanoIE -=velocidadeDoCano;
        posicaoXCanoSE -=velocidadeDoCano;

        desenhaCanoA();
        desenhaCanoB();
        desenhaCanoC();
        desenhaCanoD();
        desenhaCanoE();

        if(posicaoXCanoSA>((posicaoCanoAnstesDeEntrarNaTela)-10))
        {
            numeroRandomicoA = geraNumAleatorio();
        }
        if(posicaoXCanoSB>((posicaoCanoAnstesDeEntrarNaTela)-10))
        {
            numeroRandomicoB = geraNumAleatorio();
        }
        if(posicaoXCanoSC>((posicaoCanoAnstesDeEntrarNaTela)-10))
        {
            numeroRandomicoC = geraNumAleatorio();
        }
        if(posicaoXCanoSD>((posicaoCanoAnstesDeEntrarNaTela)-10))
        {
            numeroRandomicoD = geraNumAleatorio();
        }
        if(posicaoXCanoSE>((posicaoCanoAnstesDeEntrarNaTela)-10))
        {
            numeroRandomicoE = geraNumAleatorio();
        }
    }
    public void canosParados()
    {
        desenhaCanoA();
        desenhaCanoB();
        desenhaCanoC();
        desenhaCanoD();
        desenhaCanoE();
    }
    public void resetaCanos()
    {
        posicaoXCanoSA = ScreenSizeX+(porcentagemx*canoSuperior.getWidth());
        posicaoXCanoIA = ScreenSizeX+(porcentagemx*canoInferior.getWidth());

        posicaoXCanoSB = (posicaoXCanoSA+ distanciaEntreProximoCano);
        posicaoXCanoIB = (posicaoXCanoSA+distanciaEntreProximoCano);

        posicaoXCanoSC = (posicaoXCanoSB+distanciaEntreProximoCano);
        posicaoXCanoIC = (posicaoXCanoSB+distanciaEntreProximoCano);

        posicaoXCanoSD =(posicaoXCanoSC+distanciaEntreProximoCano);
        posicaoXCanoID =(posicaoXCanoSC+distanciaEntreProximoCano);

        posicaoXCanoSE =(posicaoXCanoSD+distanciaEntreProximoCano);
        posicaoXCanoIE =(posicaoXCanoSD+distanciaEntreProximoCano);
    }
    public void definindoVelocidadeDoCano()
    {
        //deltaTime = Gdx.graphics.getDeltaTime();
        //velocidadeDoCano = deltaTime*350;
        velocidadeDoCano = deltaTime;
        //Gdx.app.log("velocidade", "velocidadedocano :"+velocidadeDoCano);
        //Gdx.app.log("velocidade", "velocidadedelta :"+deltaTime);
    }
    public void limpaTela()
    {
        //linha Extremamente Necessária!
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    public void animaPassaro()
    {
        variacao+= (Gdx.graphics.getDeltaTime()+0.1f);
        if(variacao>2)
        {
            variacao=0;
        }
    }
    public void tocaSom(String x)
    {
        if(x.contains("baterAsa")&& tocouTela==false)
        {
            if (!(som.isPlaying()))
            {
                    som.play();
            }
//            if (som.isPlaying())
//            {
//                som.dispose();
//                som = Gdx.audio.newMusic(Gdx.files.getFileHandle("audio/wing2.mp3", Files.FileType.Internal));
//                som.play();
//            }
        }



    }
    public int geraNumAleatorio()
    {
        geradorDeRandomicoCurto = new Random();
        numeroRandParaBool = geradorDeRandomicoCurto.nextInt(2);

        geradorDeRandomico = new Random();

        //Gdx.app.log("seraaa",""+numeroRandParaBool);
        if(numeroRandParaBool==0)
        {
            return geradorDeRandomico.nextInt((int) variacaoDeCriacaoDeCanos);
        }
        else
        {
            return (-1*(geradorDeRandomico.nextInt((int) variacaoDeCriacaoDeCanos)));
        }

    }
    public void checaPassaroXCanoPontos()
    {
        if(posicaoX> (posicaoXCanoSA+larguraDoCanoS-10)&& posicaoX< ((posicaoXCanoSA+larguraDoCanoS)-3))
        {
                pontuacao++;
        }
        if(posicaoX> (posicaoXCanoSB+larguraDoCanoS-10)&& posicaoX< ((posicaoXCanoSB+larguraDoCanoS)-3))
        {
            pontuacao++;
        }
        if(posicaoX> (posicaoXCanoSC+larguraDoCanoS-10)&& posicaoX< ((posicaoXCanoSC+larguraDoCanoS)-3))
        {
            pontuacao++;
        }
        if(posicaoX> (posicaoXCanoSD+larguraDoCanoS-10)&& posicaoX< ((posicaoXCanoSD+larguraDoCanoS)-3))
        {
            pontuacao++;
        }
        if(posicaoX> (posicaoXCanoSE+larguraDoCanoS-10)&& posicaoX< ((posicaoXCanoSE+larguraDoCanoS)-3))
        {
            pontuacao++;
        }
    }
    public void checaPassaroXCanoColisao()
    {
        if(posicaoX-(tamanhoPassaroX)< posicaoXCanoSA && posicaoX+(tamanhoPassaroX-5)>posicaoXCanoSA)
        {
            if((movimento+tamanhoPAssaroY)>(pocicaoYCanoSA-5)||movimento<(pocicaoYCanoIA+alturaDoCanoI))
            {
                estadoDoJogo=2;
            }
        }
        if(posicaoX-(tamanhoPassaroX) < posicaoXCanoSB && posicaoX+(tamanhoPassaroX-5)>posicaoXCanoSB)
        {
            if((movimento+tamanhoPAssaroY)>(pocicaoYCanoSB-5)||movimento<(pocicaoYCanoIB+alturaDoCanoI))
            {
                estadoDoJogo=2;
            }
        }
        if(posicaoX-(tamanhoPassaroX) < posicaoXCanoSC && posicaoX+(tamanhoPassaroX-5)>posicaoXCanoSC)
        {
            if((movimento+tamanhoPAssaroY)>(pocicaoYCanoSC-5)||movimento<(pocicaoYCanoIC+alturaDoCanoI))
            {
                estadoDoJogo=2;
            }
        }
        if(posicaoX-(tamanhoPassaroX) < posicaoXCanoSD && posicaoX+(tamanhoPassaroX-5)>posicaoXCanoSD)
        {
            if((movimento+tamanhoPAssaroY)>(pocicaoYCanoSD-5)||movimento<(pocicaoYCanoID+alturaDoCanoI))
            {
                estadoDoJogo=2;
            }
        }
        if(posicaoX-(tamanhoPassaroX+3) < posicaoXCanoSE && posicaoX+tamanhoPassaroX>posicaoXCanoSE)
        {
            if((movimento+tamanhoPAssaroY)>(pocicaoYCanoSE-5)||movimento<(pocicaoYCanoIE+alturaDoCanoI))
            {
                estadoDoJogo=2;
            }
        }
    }

}
