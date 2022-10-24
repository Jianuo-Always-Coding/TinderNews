package com.laioffer.tinnews.ui.home;

public class OuterClass {

    static void main() {
        Inner inner = new OuterClass().new Inner();
    }

    public void run() {

    }

    public class Inner {

        void main() {
            run();
        }
    }
}
