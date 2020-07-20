package com.akane.scarletserenity.model;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class CharacterHelper  {
    private static final String COLLECTION_NAME = "characters";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getCharacterCollection(String uid){
        return UserHelper.getUsersCollection().document(uid).collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createCharacter(String uid, String pseudo, String gender, Integer money, Integer hpMax, Integer manaMax, Integer staminaMax, Integer hungerMax,
                                             Integer hp, Integer mana, Integer stamina, Integer hunger, Integer strength, Integer intelillgence,
                                             Integer agility, Integer luck, String created, String lastLogin) {
        Character characterToCreate = new Character(pseudo, gender, money,hpMax,manaMax,staminaMax,hungerMax,hp,mana,stamina,hunger,strength,intelillgence,agility,luck,
                created,lastLogin);
        return CharacterHelper.getCharacterCollection(uid).document("character").set(characterToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getCharacter(String uid){
        return CharacterHelper.getCharacterCollection(uid).document("character").get();
    }

    // --- UPDATE ---

    public static Task<Void> updatePseudo( String uid, String pseudo) {
        return CharacterHelper.getCharacterCollection(uid).document("character").update("pseudo", pseudo);
    }

    // Rajouter les différentes stats

    public static Task<Void> updateStrength(String uid, int strength) {
        return CharacterHelper.getCharacterCollection(uid).document("character").update("strength", strength);
    }

    public static Task<Void> updateHpMax(String uid, int strength) {
        return CharacterHelper.getCharacterCollection(uid).document("character").update("hpMax", 25*strength);
    }

    public static Task<Void> updateIntelligence(String uid, int intelligence) {
        return CharacterHelper.getCharacterCollection(uid).document("character").update("intelligence", intelligence);
    }

    public static Task<Void> updateManaMax(String uid, int intelligence) {
        return CharacterHelper.getCharacterCollection(uid).document("character").update("manaMax", 20*intelligence);
    }

    public static Task<Void> updateAgility(String uid, int agility) {
        return CharacterHelper.getCharacterCollection(uid).document("character").update("agility", agility);
    }

    public static Task<Void> updateLuck(String uid, int luck) {
        return CharacterHelper.getCharacterCollection(uid).document("character").update("luck", luck);
    }


    // --- DELETE ---

    public static Task<Void> deleteCharacter(String uid) {
        return CharacterHelper.getCharacterCollection(uid).document("character").delete();
    }
}
