package com.example.Demo_Subj;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by johannes on 22.11.2014.
 */

public class Subject {

    //aktuelle Position
    private float xPos;
    private float yPos;
    private int aktRoomID = 1;

    //Ziel
    private float xDest;
    private int destRoomID;

    //Bildverweise
    private Bitmap subjStandBitmap;
    private Bitmap subjStandBitmapInv;

    private List<Bitmap> subjectWalk;
    private List<Bitmap> subjectWalkInv;

    private Bitmap aktBitmap;

    private int listPointerWalk = 0;
    private int holdAnimation = 0;
    private static int holdAnimationCycles = 20;

    private Intelligence intel;

    private Sound sound;

    //Variablen für die (noch einzufügenden) verschiedenen Animationen
    private int animation = 0;          //0 = laufen, 1 = ... usw.


    public Subject(Context ctx, Resources resources){
        intel = new Intelligence();
        sound = new Sound(ctx);

        fillWalkingArrayLists(resources);

        float canvasx = (float) GlobalInformation.getScreenWidth();
        float canvasy = (float) GlobalInformation.getScreenHeight();
        float bitmapx = (float) subjStandBitmap.getWidth();
        float bitmapy = (float) subjStandBitmap.getHeight();
        float posX = ((canvasx/2) - (bitmapx / 2));
        float posY = (((canvasy/2) - (bitmapy / 2)) + (canvasy/15));
        setDefaultKoords(posX, posY, aktRoomID);
    }

    private void setDefaultKoords(float xDef, float yDef, int room) {
        xPos = xDef;
        yPos = yDef;
        aktRoomID = room;

        xDest = xDef;
        destRoomID = room;
    }

    private void fillWalkingArrayLists(Resources resources){                                        //Existiert temporär, wird ins Subjekt verschoben

        List<Bitmap> subjectWalkingList;                                                                    //Animation des Subjekts und Laufens laden
        Bitmap bm;                                                                                          //temporäre Bitmap Variable zum spiegeln und befüllen der Arrays

        Bitmap subjectBStand = BitmapFactory.decodeResource(resources, R.drawable.subjekt);
        subjectWalkingList = new ArrayList<Bitmap>();
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_1));
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_2));
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_3));
        subjectWalkingList.add(BitmapFactory.decodeResource(resources, R.drawable.walk_4));

        subjStandBitmap = Bitmap.createScaledBitmap(subjectBStand, 170, 330, false);
        subjStandBitmapInv = mirrorBitmap(subjStandBitmap);

        aktBitmap = subjStandBitmap;

        subjectWalk = new ArrayList<Bitmap>();                                                              //Arrays für die einzelnen Bilder der Animation (vorwärts und rückwärts)
        subjectWalkInv = new ArrayList<Bitmap>();

        for (int i = 0; i <= (subjectWalkingList.size() - 1); i++){                                         //Vorwärtsarray befüllen
            bm = subjectWalkingList.get(i);
            subjectWalk.add(i,  Bitmap.createScaledBitmap(bm, 170, 330, false));
        }

        for (int i = 0; i <= (subjectWalk.size() - 1); i++){                                                //Rückwärtsarray ist gespiegeltes Vorwärtsarray
            bm = subjectWalk.get(i);
            bm = this.mirrorBitmap(bm);
            subjectWalkInv.add(i, bm);
        }
    }

    private void fillObjectAnimList(Resources resources){                                       //Existiert temporär, bis der XML-Parser eingebunden wurde
        //eine globale Liste für Objektanimationen wird hier befüllt
        //die Variable animation steht für eine Animation, die ausgeführt werden soll
        //die switch-case Verzweigung in der Methode onDraw muss zusätzlich erweitert werden
    }

    private void calculatePosition(){
        if((xPos == xDest) && (aktRoomID == destRoomID)){                   //Animationen basierend auf den Aufrufen
            aktBitmap = subjStandBitmap;
            listPointerWalk = 0;
            SubjectMoveAction nextAction = intel.getNextAction();
            setDest(nextAction.getDestX(), nextAction.getDestRoom());
            // move.getDestItem().use();
        }
        else if (aktRoomID == destRoomID) {
            if (xPos > xDest) {
                swapBitmap(false);
                xPos--;
                xPos--;                                                     //Subjekt schneller
            }
            else{
                swapBitmap(true);
                xPos++;
                xPos++;                                                     //Subjekt schneller
            }
        }
        else if (aktRoomID > destRoomID){
            if (xPos > 0){
                swapBitmap(false);
                xPos--;
                xPos--;                                                     //Subjekt schneller
            }
            else{
                aktBitmap = subjStandBitmap;
                aktRoomID--;
                GlobalInformation.setCurrentRoom(aktRoomID);                        //Kommt später in Subject
                xPos = GlobalInformation.getScreenWidth();
                //reset walking animation
                listPointerWalk = 0;
                sound.startSound(R.raw.sound_door);
            }
        }
        else if (aktRoomID < destRoomID){
            if (xPos < (GlobalInformation.getScreenWidth())){
                swapBitmap(true);
                xPos++;
                xPos++;                                                     //Subjekt schneller
            }
            else{
                aktBitmap = subjStandBitmapInv;
                aktRoomID++;
                GlobalInformation.setCurrentRoom(aktRoomID);                        //Kommt später in Subject
                xPos = 0;
                //reset walking animation
                listPointerWalk = 0;
                sound.startSound(R.raw.sound_door);
            }
        }
    }

    //Methoden aus Subject
    /*****************************************************************************************/
    public void setDest(float x, int room){
        xDest = x;
        destRoomID = room;
    }

    private void swapBitmap(boolean mirror){
        if (mirror == true){
            if (holdAnimation < holdAnimationCycles){
                holdAnimation++;
            }
            else{
                holdAnimation = 0;
                aktBitmap = subjectWalkInv.get(listPointerWalk);
                listPointerWalk++;
                if (listPointerWalk > (subjectWalkInv.size() - 1)){
                    listPointerWalk = 0;
                }
            }
        }
        else
        if (holdAnimation < holdAnimationCycles){
            holdAnimation++;
        }
        else {
            holdAnimation = 0;
            aktBitmap = subjectWalk.get(listPointerWalk);
            listPointerWalk++;
            if (listPointerWalk > (subjectWalk.size() - 1)) {
                listPointerWalk = 0;
            }
        }
    }

    private Bitmap mirrorBitmap(Bitmap b){
        Matrix matrixMirror = new Matrix();
        matrixMirror.preScale(-1.0f, 1.0f);
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrixMirror, false);
        return b;
    }

    public void tick(){
        //Animation für das Subjekt, übernommen aus Subjekt
        /*****************************************************************************************/
        intel.tick();                   //KI aufrufen
        //hier muss später noch ein Aufruf zum ändern der Animation eingefügt werden, z.B. durch die KI oder durch RoomActivity selbst
        /*switch (animation) {
            case 0:     calculatePosition();
                break;

            //Weiter Switch-Cases für weitere Animationen

            default:    aktBitmap = subjStandBitmap;        //entweder das oder eine Fehlermeldung
                break;
        }*/
        calculatePosition();
    }

    public Bitmap getBitmap(){
        return aktBitmap;
    }

    public float getXPos(){
        return xPos;
    }

    public float getYPos(){
        return yPos;
    }
    /*
    private void startSound(int soundRes){
        Uri path = Uri.parse("android.resource://com.example.Demo_Subj/" + soundRes);
        mediaplayer = new MediaPlayer();
        mediaplayer.reset();
        try{
            mediaplayer.setDataSource(context, path);
        }catch (IOException e){
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        try{
            mediaplayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }

        Thread sound = new Thread(new Runnable() {
            @Override
            public void run() {
            mediaplayer.start();
            mediaplayer.setVolume(1.0f, 1.0f);

            AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume (AudioManager.STREAM_MUSIC), 0);

            while (mediaplayer.getCurrentPosition() != mediaplayer.getDuration()) {

            }
            mediaplayer.start();
            mediaplayer.setVolume(1,1);
            }
        });
        sound.start();
    }*/

}
