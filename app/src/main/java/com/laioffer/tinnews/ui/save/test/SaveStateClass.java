package com.laioffer.tinnews.ui.save.test;

public class SaveStateClass {
    private int a = 0;
    private int b = 0;

    // java I know this
    SaveStateClass() {

    }

    // developer you both of them
    SaveStateClass(int a, int b) {
        this.b = a;
        this.a = b;
    }

    public void guess() {
        a = 5;
    }
}
