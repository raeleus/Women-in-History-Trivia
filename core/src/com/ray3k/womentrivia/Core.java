package com.ray3k.womentrivia;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Core extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private Array<Question> questions;
    private static final float SPEED_X = -10.0f;
    private static final float SPEED_Y = -10.0f;
    private Sound winSound;
    private Sound loseSound;
    private Sound beepSound;
    private Music music;

    @Override
    public void create() {
        questions = new Array<Question>();
        createQuestions();
        
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.play();
        
        winSound = Gdx.audio.newSound(Gdx.files.internal("win.wav"));
        loseSound = Gdx.audio.newSound(Gdx.files.internal("lose.wav"));
        beepSound = Gdx.audio.newSound(Gdx.files.internal("beep.wav"));
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        skin = new Skin(Gdx.files.internal("Women in History Trivia.json"));
        
        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(skin.getDrawable("bg-tiled"));
        stage.addActor(root);
        
        root.defaults().space(50.0f);
        Image image = new Image(skin, "logo");
        image.setScaling(Scaling.none);
        root.add(image);
        
        root.row();
        Table table = new Table();
        table.setBackground(skin.getDrawable("black"));
        root.add(table);
        
        int rows = 5;
        int columns = 5;
        float border = 10.0f;
        table.defaults().space(border);
        
        int i = 0;
        int value = 100;
        for (int y = 0; y < columns; y++) {
            for (int x = 0; x < rows; x++) {
                final TextButton textButton = new TextButton("$" + Integer.toString(value), skin, "screen");
                table.add(textButton).size(90.0f, 60.0f);
                
                final int questionIndex = i++;
                textButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event,
                            Actor actor) {
                        music.setVolume(.25f);
                        beepSound.play();
                        showQuestionDialog(questions.get(questionIndex));
                        textButton.setDisabled(true);
                    }
                });
                
                if (x == 0) {
                    table.getCell(textButton).padLeft(border);
                }
                
                if (x == rows - 1) {
                    table.getCell(textButton).padRight(border);
                }
                
                if (y == 0) {
                    table.getCell(textButton).padTop(border);
                }
                
                if (y == columns - 1) {
                    table.getCell(textButton).padBottom(border);
                }
            }
            value += 100;
            table.row();
        }
    }
    
    private void showQuestionDialog(Question question) {
        final Dialog dialog = new Dialog("", skin);
        dialog.setFillParent(true);
        
        Label label = new Label(question.getQuestion(), skin, "question");
        label.setWrap(true);
        label.setAlignment(Align.center);
        dialog.getContentTable().add(label).growX();
        
        Table table = new Table();
        dialog.getButtonTable().add(table);
        
        table.defaults().space(5.0f);
        for (int i = 0; i < question.getAnswers().size; i++) {
            String answer = question.getAnswers().get(i);
            final TextButton textButton = new TextButton(answer, skin);
            table.add(textButton).growX();
            
            if (i == question.getCorrectIndex()) {
                textButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event,
                            Actor actor) {
                        dialog.hide();
                        winSound.play();
                        music.setVolume(1.0f);
                    }
                });
            } else {
                textButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event,
                            Actor actor) {
                        textButton.setDisabled(true);
                        loseSound.play();
                    }
                });
            }
            table.row();
        }
        
        dialog.getButtonTable().getCells().get(dialog.getButtonTable().getCells().size - 1).padBottom(25.0f);
        
        dialog.show(stage);
    }
    
    private void createQuestions() {
        questions.clear();
        
        questions.add(new Question("The first woman to have flown in space", 2, "Who is Anna Pavlova?", "Who is Martina Navratilova?", "Who is Valentina Tereshkova?", "Who is Alina Zagitova?"));
        questions.add(new Question("The year women were granted the right to vote", 1, "What is the year 1900?", "What is the year 1920?", "What is the year 1940?", "What is the year 1960?"));
        questions.add(new Question("The author of Frankenstein", 3, "Who is Anne Frank?", "Who is Jane Austen?", "Who is J. K. Rowling?", "Who is Mary Shelley?"));
        questions.add(new Question("A Nobel Prize winning physicist and chemist", 0, "Who is Marie Curie?", "Who is Susan B. Anthony?", "Who is Emily Dickinson?", "Who is Helen Keller?"));
        questions.add(new Question("The last pharoah of ancient Egypt", 2, "Who is Susanna Hoffs?", "Who is Nefertiti?", "Who is Cleopatra?", "Who is Anaksunamun?"));
        questions.add(new Question("A Jewish diarist who went into hiding to escape the Nazis", 1, "Who is Anne Hathaway?", "Who is Anne Frank?", "Who is Anna Kournikova?", "Who is Anna Karina?"));
        questions.add(new Question("A talk show host and philanthropist who is most well known for her titular TV show", 3, "Who is Jessica Chastain?", "Who is Whoopi Goldberg?", "Who is Lucy Lawless?", "Who is Oprah Winfrey?"));
        questions.add(new Question("The author of the famous Harry Potter series of books", 0, "Who is J. K. Rowling?", "Who is Anne Frank?", "Who is Jane Austen?", "Who is Mary Shelley?"));
        questions.add(new Question("Actress and inventor whose work led to the creation of Bluetooth and WiFi", 1, "Who is Marilyn Monroe?", "Who is Heddy Lamar?", "Who is Bette Davis?", "Who is Jane Fonda?"));
        questions.add(new Question("Famed English author of Sense and Sensibility, Pride and Prejudice, and Emma", 1, "Who is J. K. Rowling?", "Who is Jane Austen?", "Who is Anne Frank?", "Who is Mary Shelley?"));
        questions.add(new Question("Abolitionist and women’s rights activist who paved the way for women’s suffrage", 3, "Who is Patty Jenkins?", "Who is Audrey Hepburn?", "Who is Aretha Franklin?", "Who is Susan B. Anthony?"));
        questions.add(new Question("The right to vote in political elections", 2, "What is judiciousness?", "What is escrow?", "What is suffrage?", "What is deposition?"));
        questions.add(new Question("American poet who secretly wrote close to 1,800 poems in her lifetime", 0, "Who is Emily Dickinson?", "Who is Anne Frank?", "Who is Jane Austen?", "Who is J. K. Rowling?"));
        questions.add(new Question("Deaf and blind at an early age, she was an author, lecturer, and activist for the deaf and blind", 3, "Who is Helen Woodward?", "Who is Helen Hunt?", "Who is Helen Troy?", "Who is Helen Keller?"));
        questions.add(new Question("Aviator and first woman to fly solo across the Atlantic Ocean", 0, "Who is Amelia Earhart?", "Who is Donna Flifar?", "Who is Gena Plyanride?", "Who is Lauren Skisole?"));
        questions.add(new Question("A civil rights activist most known for resisting bus segregation by not giving up her seat", 1, "Who is Susan B. Anthony?", "Who is Rosa Parks?", "Who is Harriet Tubman?", "Who is Jane Goodall?"));
        questions.add(new Question("A professional American tennis player often cited as the greatest female player of all time", 2, "Who is Laila Ali?", "Who is Gina Carano?", "Who is Serena Williams?", "Who is Gal Gadot?"));
        questions.add(new Question("Conservationist considered by many to be the foremost expert on chimpanzees", 1, "Who is Jane Austen?", "Who is Jane Goodall?", "Who is Jane Fonda?", "Who is Jane Foster?"));
        questions.add(new Question("An early civil rights activist and humanitarian", 0, "Who is Harriet Tubman?", "Who is Jane Goodall?", "Who is Aretha Franklin?", "Who is Kathryn Bigelow?"));
        questions.add(new Question("A cultural icon from World War II that inspired women to take new jobs normally held by men", 2, "Who is Lucy the Loader?", "Who is Mary the Machinist?", "Who is Rosie the Riveter?", "Who is Candy the Carpenter?"));
        questions.add(new Question("An interpreter who aided the Lewis and Clark Expedition", 1, "Who is Pocahontas?", "Who is Sacagawea?", "Who is Maria Tallchief?", "Who is Ada Deer?"));
        questions.add(new Question("The condition of being deprived of a right such as the right to vote", 2, "What is fraternization?", "What is disparate?", "What is disenfranchisement?", "What is suffering?"));
        questions.add(new Question("Coined in the 1920’s, a generation of women who listened to Jazz and behaved against convention", 1, "Who were the hipsters?", "Who were the flappers?", "Who were the suffragettes?", "Who were the beliebers?"));
        questions.add(new Question("Women who speak out for the women's right to vote", 3, "What is the franchisement board?", "Who are the flappers?", "What is the League of Exceptional Women?", "Who are the suffragettes?"));
        questions.add(new Question("A belief that advocates equal rights for women", 2, "What is sisterhood?", "What is the fifth ammendment?", "What is feminism?", "What is the lodge of womanly interests?"));
        
        questions.shuffle();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        
        Gdx.gl.glClearColor(138 / 255.0f, 168 / 255.0f, 186 / 255.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
        ScrollingTiledDrawable drawable = (ScrollingTiledDrawable) skin.getDrawable("bg-tiled");
        drawable.setOffsetX(drawable.getOffsetX() + SPEED_X * delta);
        drawable.setOffsetY(drawable.getOffsetY() + SPEED_Y * delta);
        
        stage.act();
        stage.draw();
        
        if (Gdx.input.isKeyJustPressed(Keys.F5)) {
            dispose();
            create();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        music.stop();
    }
}
