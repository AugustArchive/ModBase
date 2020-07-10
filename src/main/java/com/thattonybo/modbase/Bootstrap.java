package com.thattonybo.modbase;

import com.thattonybo.modbase.core.ModBaseImpl;

public class Bootstrap {
    public static void main(String[] args) {
        ModBaseImpl impl = new ModBaseImpl();

        try {
            impl.load();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
