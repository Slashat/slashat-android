slashat-android
===============

Slashat.se för Android-enheter

Krav
-------------
* Skall funka ner till Android 2.1, API Level: 7, Eclair.
* Kräver ActionBarSherlock för att ge oss fragment-stöd ner till 2.1 (se sektion nedan för mer info om ActionBarSherlock)
* Kommentarer i koden skall skrivas på engelska. I övriga fall avgör ni själva om ni vill skriva på svenska eller engelska.

ActionBarSherlock 
-------------
Appen kräver att ActionBarSherlock (http://actionbarsherlock.com/) ligger i en mapp brevid slashat-android-mappen. Det krävs även att ni importerat ActionBarSherlock in i Eclipse så länkningen funkar korrekt vid kompilering. Man skall även lägga till android-support-v4.jar ifrån ActionBarSherlock mappen. (se till att hela ActionBarSherlock-mappen ligger i parent dir till android-slashat-mappen) samt köra new Android project from existing source och välja Sherlock mappen och sedan välja library projektet. Först efter detta steget så hittar eclipse alla ActionBarSherlock referenser korrekt.
