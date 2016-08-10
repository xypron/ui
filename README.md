About
=====

Xypron GUI Components is a class library written in Java.
It facilitates writing Swing applications.

Maven
-----

For using this library in your Maven project enter the
following dependency in your pom.xml:


    <dependencies>
        <dependency>
            <groupId>de.xypron.ui</groupId>
            <artifactId>ui</artifactId>
            <version>1.0.8</version>
        </dependency>
    </dependencies>

Examples
========

Minimum example
---------------

Each application using the GUI library will subclass IdeApplication and the
override some of the methods to add its own functionality.

A minimum example that does not add any functionality is:

    package de.xypron.ui.example;
    
    import de.xypron.ui.components.IdeApplication;
    
    public class Example1 extends IdeApplication
    {
        public static void main( String[] args )
        {
            new Example1().run();
        }
    }

IconBuffer and IdeText
----------------------

The GUI library provides class IconBuffer to create objects of type ImageIcon.
The icon files have to be stored in path src/main/resources.

Furthermore text for the GUI can read from resource bundles using class
IdeText. For the example below create file strings.properties in the
src/main/ressources/de/xypron/ui/example path with the following content:

    Tab.title=Output
    Tab.tip=Output goes here

Read the javadoc of java.util.ResourceBundle for details about resource bundles.

    package de.xypron.ui.example;
    
    import de.xypron.ui.components.IdeApplication;
    import de.xypron.ui.components.IdePanel;
    import de.xypron.util.IconBuffer;
    import de.xypron.util.IconName;
    
    @IconName("de/xypron/ui/components/icon.png")
    public class Example2 extends IdeApplication {
    
        private static final String TAB_KEY = "TAB";

        public static void main(String[] args) {
            new Example2().run();
        }
    
        @Override
        protected void init() {
            this.getIdeTabbedPane().setComponent(
                    TAB_KEY, new Tab(), getText("Tab.title"),
                    IconBuffer.getIcon(Tab.class), getText("Tab.tip"), true);
        }
    
        @IconName("de/xypron/ui/components/result.png")
        private class Tab extends IdePanel {
        }
    }

