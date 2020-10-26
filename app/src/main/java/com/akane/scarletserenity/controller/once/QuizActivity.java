package com.akane.scarletserenity.controller.once;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.akane.scarletserenity.controller.BaseActivity;
import com.akane.scarletserenity.model.character.Character;
import com.akane.scarletserenity.model.character.CharacterHelper;
import com.akane.scarletserenity.model.quiz.Question;
import com.akane.scarletserenity.model.quiz.QuestionBank;
import com.akane.scarletserenity.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.time.LocalDateTime;
import java.util.Arrays;

public class QuizActivity extends BaseActivity implements View.OnClickListener{

    private MediaPlayer mp;
    private LocalDateTime current;
    private int seconde;

    Integer strength;
    Integer intelligence;
    Integer agility;
    Integer luck;

    private TextView gQuestionTextView;
    private Button gAnswer1Button;
    private Button gAnswer2Button;
    private Button gAnswer3Button;
    private Button gAnswer4Button;

    private QuestionBank questionBank;
    private Question currentQuestion;

    private int numberOfQueestions;

    private boolean enableTouchEvents;

    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //music
        mp =  MediaPlayer.create(this,R.raw.rereation);
        mp.setLooping(true);
        mp.setVolume(0.7f,0.7f);
        mp.start();

        current = LocalDateTime.now();
        seconde = current.getSecond();
        Log.d("time", "time "+ current);
        questionBank = this.generateQuestions();

        numberOfQueestions = 10;
        enableTouchEvents = true;

        // Widgets
        gQuestionTextView = findViewById(R.id.activity_game_question_text);
        randomAnswer(seconde);

        currentQuestion = questionBank.getQuestion();
        this.displayQuestion(currentQuestion);

        getCharacter();

        if (savedInstanceState != null) {
            numberOfQueestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            Toast.makeText(this,"Bug exploitable !",Toast.LENGTH_SHORT).show();
        }
    }


    private void displayQuestion(final Question question) {
        gQuestionTextView.setText(question.getQuestion());
        gAnswer1Button.setText(question.getChoiceList().get(0));
        gAnswer2Button.setText(question.getChoiceList().get(1));
        gAnswer3Button.setText(question.getChoiceList().get(2));
        gAnswer4Button.setText(question.getChoiceList().get(3));
    }


    private QuestionBank generateQuestions(){

        Question question1 = new Question(getString(R.string.question1),
                Arrays.asList(getString(R.string.a1question1), getString(R.string.a2question1), getString(R.string.a3question1), getString(R.string.a4question1)));

        Question question2 = new Question(getString(R.string.question2),
                Arrays.asList(getString(R.string.a1question2), getString(R.string.a2question2), getString(R.string.a3question2), getString(R.string.a4question2)));

        Question question3 = new Question(getString(R.string.question3),
                Arrays.asList(getString(R.string.a1question3), getString(R.string.a2question3), getString(R.string.a3question3), getString(R.string.a4question3)));

        Question question4 = new Question(getString(R.string.question4),
                Arrays.asList(getString(R.string.a1question4), getString(R.string.a2question4), getString(R.string.a3question4), getString(R.string.a4question4)));

        Question question5 = new Question(getString(R.string.question5),
                Arrays.asList(getString(R.string.a1question5), getString(R.string.a2question5), getString(R.string.a3question5), getString(R.string.a4question5)));

        Question question6 = new Question(getString(R.string.question6),
                Arrays.asList(getString(R.string.a1question6), getString(R.string.a2question6), getString(R.string.a3question6), getString(R.string.a4question6)));

        Question question7 = new Question(getString(R.string.question7),
                Arrays.asList(getString(R.string.a1question7), getString(R.string.a2question7), getString(R.string.a3question7), getString(R.string.a4question7)));

        Question question8 = new Question(getString(R.string.question8),
                Arrays.asList(getString(R.string.a1question8), getString(R.string.a2question8), getString(R.string.a3question8), getString(R.string.a4question8)));

        Question question9 = new Question(getString(R.string.question9),
                Arrays.asList(getString(R.string.a1question9), getString(R.string.a2question9), getString(R.string.a3question9), getString(R.string.a4question9)));

        Question question10 = new Question(getString(R.string.question10),
                Arrays.asList(getString(R.string.a1question10), getString(R.string.a2question10), getString(R.string.a3question10), getString(R.string.a4question10)));


        return new QuestionBank(Arrays.asList(

                question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9,
                question10
        ));

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return enableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        if (responseIndex == 0) {
            // strenght
            Toast.makeText(this,"Power !",Toast.LENGTH_SHORT).show();
            getCharacter();
            CharacterHelper.updateStrength(user.getUid(), strength + 5);
            CharacterHelper.updateHpMax(user.getUid(), strength);
        } else if (responseIndex == 1){
            // intelligence
            Toast.makeText(this,"Intelligence !",Toast.LENGTH_SHORT).show();
            getCharacter();
            CharacterHelper.updateIntelligence(user.getUid(),intelligence + 5);
            CharacterHelper.updateManaMax(user.getUid(),intelligence);
        } else if (responseIndex == 2){
            // agility
            Toast.makeText(this,"Agility  !",Toast.LENGTH_SHORT).show();
            getCharacter();
            CharacterHelper.updateAgility(user.getUid(),agility + 5);
        } else if (responseIndex == 3){
            // luck
            Toast.makeText(this,"Lucky  !",Toast.LENGTH_SHORT).show();
            getCharacter();
            CharacterHelper.updateLuck(user.getUid(),luck + 5);
        }
        enableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enableTouchEvents = true;

                if (--numberOfQueestions == 0) {
                    // Pas de questions restantes donc terminer le quiz
                    endGame();
                } else {
                    currentQuestion = questionBank.getQuestion();
                    displayQuestion(currentQuestion);
                    current = LocalDateTime.now();
                    seconde = current.getSecond();
                //    Log.d("time", "time "+ second);
                   //randomAnswer(seconde);
                }
            }
        }, 2000); // delay


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(BUNDLE_STATE_QUESTION, numberOfQueestions);

        super.onSaveInstanceState(outState);
    }


    private void getCharacter(){

            CharacterHelper.getCharacter(user.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Character character = documentSnapshot.toObject(Character.class);
                    strength = character.getStrength();
                    intelligence = character.getIntelligence();
                    agility = character.getAgility();
                    luck = character.getLuck();
                }
            });
        }




    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        getCharacter();

        if (strength > intelligence && strength > agility && strength > luck) {
            CharacterHelper.updateStrength(user.getUid(), strength + 20);
            CharacterHelper.updateHpMax(user.getUid(), strength);
            builder.setTitle(getString(R.string.quiz_strenght_title))
                    .setMessage(getString(R.string.quiz_strenght_description))
                    .setPositiveButton(getString(R.string.quiz_end), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSynopsisActivity();
                            finish();
                        }
                    })
                    .create()
                    .show();
        } else if (intelligence > strength && intelligence > agility && intelligence > luck) {
            CharacterHelper.updateIntelligence(user.getUid(),intelligence + 20);
            CharacterHelper.updateManaMax(user.getUid(),intelligence);
            builder.setTitle(getString(R.string.quiz_int_title))
                    .setMessage( getString(R.string.quiz_int_description))
                    .setPositiveButton(getString(R.string.quiz_end), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSynopsisActivity();
                            finish();
                        }
                    })
                    .create()
                    .show();
        } else if (agility > strength && agility > intelligence && agility > luck) {
            CharacterHelper.updateAgility(user.getUid(),agility + 20);
            builder.setTitle(getString(R.string.quiz_agility_title))
                    .setMessage(getString(R.string.quiz_agility_description))
                    .setPositiveButton(getString(R.string.quiz_end), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSynopsisActivity();
                            finish();
                        }
                    })
                    .create()
                    .show();
        } else if(luck > strength && luck > agility && luck > intelligence) {
            CharacterHelper.updateLuck(user.getUid(),luck + 20);
            builder.setTitle(getString(R.string.quiz_luck_title))
                    .setMessage(getString(R.string.quiz_luck_description))
                    .setPositiveButton(getString(R.string.quiz_end), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSynopsisActivity();
                        }
                    })
                    .create()
                    .show();
        } else { // stats egaux
            CharacterHelper.updateStrength(user.getUid(), strength + 7);
            CharacterHelper.updateHpMax(user.getUid(), strength);

            CharacterHelper.updateIntelligence(user.getUid(),intelligence + 7);
            CharacterHelper.updateManaMax(user.getUid(),intelligence);
            CharacterHelper.updateAgility(user.getUid(),agility + 7);
            CharacterHelper.updateLuck(user.getUid(),luck + 7);

            builder.setTitle(getString(R.string.quiz_all_title))
                    .setMessage(getString(R.string.quiz_all_description))
                    .setPositiveButton(getString(R.string.quiz_end), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startSynopsisActivity();
                        }
                    })
                    .create()
                    .show();
        }

    }

    private void startSynopsisActivity() {
        mp.stop();
        mp.release();
        Intent intent = new Intent(QuizActivity.this, SynopsisActivity.class);
        startActivity(intent);
        setResult(RESULT_OK, intent);
    }

    private int getLastSecond(int second) {
        int lastSecond = second;
    if(lastSecond > 50){
        lastSecond -= 50;
    } else if(lastSecond > 40){
        lastSecond -= 40;
    } else if(lastSecond > 30){
        lastSecond -= 30;
    } else if(lastSecond > 20){
        lastSecond -= 20;
    } else if(lastSecond > 10){
        lastSecond -= 10;
    }

    return lastSecond;
    }

    private void randomAnswer(int second){
        if (getLastSecond(second) >= 8 )
        {
            gAnswer1Button = findViewById(R.id.activity_game_answer1_btn);
            gAnswer2Button = findViewById(R.id.activity_game_answer2_btn);
            gAnswer3Button = findViewById(R.id.activity_game_answer3_btn);
            gAnswer4Button = findViewById(R.id.activity_game_answer4_btn);
            seconde=6;
            Log.d("time", "time "+ second);
        } else if (getLastSecond(second) >= 6)
        {
            gAnswer1Button = findViewById(R.id.activity_game_answer2_btn);
            gAnswer2Button = findViewById(R.id.activity_game_answer1_btn);
            gAnswer3Button = findViewById(R.id.activity_game_answer4_btn);
            gAnswer4Button = findViewById(R.id.activity_game_answer3_btn);
            seconde=4;
        }  else if (getLastSecond(second) >= 4)
        {
            gAnswer1Button = findViewById(R.id.activity_game_answer4_btn);
            gAnswer2Button = findViewById(R.id.activity_game_answer3_btn);
            gAnswer3Button = findViewById(R.id.activity_game_answer1_btn);
            gAnswer4Button = findViewById(R.id.activity_game_answer2_btn);
            seconde=2;
        }  else if (getLastSecond(second) >= 2)
        {
            gAnswer1Button = findViewById(R.id.activity_game_answer3_btn);
            gAnswer2Button = findViewById(R.id.activity_game_answer1_btn);
            gAnswer3Button = findViewById(R.id.activity_game_answer4_btn);
            gAnswer4Button = findViewById(R.id.activity_game_answer2_btn);
            seconde=0;
        } else if (getLastSecond(second) >= 0)
        {
            gAnswer1Button = findViewById(R.id.activity_game_answer3_btn);
            gAnswer2Button = findViewById(R.id.activity_game_answer1_btn);
            gAnswer3Button = findViewById(R.id.activity_game_answer4_btn);
            gAnswer4Button = findViewById(R.id.activity_game_answer2_btn);
            seconde=8;
        }

        gAnswer1Button.setTag(0);
        gAnswer2Button.setTag(1);
        gAnswer3Button.setTag(2);
        gAnswer4Button.setTag(3);

        gAnswer1Button.setOnClickListener(this);
        gAnswer2Button.setOnClickListener(this);
        gAnswer3Button.setOnClickListener(this);
        gAnswer4Button.setOnClickListener(this);
    }
}
