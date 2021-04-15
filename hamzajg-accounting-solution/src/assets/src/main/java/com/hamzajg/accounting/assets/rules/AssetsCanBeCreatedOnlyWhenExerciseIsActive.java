package com.hamzajg.accounting.assets.rules;

public class AssetsCanBeCreatedOnlyWhenExerciseIsActive implements BusinessRule {

    @Override
    public boolean isBroken() {
        return false;
    }

    public AssetsCanBeCreatedOnlyWhenExerciseIsActive(String exerciseId) {

    }

    @Override
    public String message() {
        return "Assets Can Be Created Only When Exercise Is Active.";
    }
}
