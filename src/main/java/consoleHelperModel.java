public class consoleHelperModel implements Runnable{
    private Thread t;
    private final String threadName;
    private byte anim;

    consoleHelperModel( String name) {
        threadName = name;

    }

    public void print(String line) {
        //clear the last line if longer
        /*
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
         */
        //System.out.print("\r");
        System.out.print("\r" + line);
    }

    public void animate() {
        switch (anim) {
            case 1:
                print("[ \\ ] " + "Loading" + "");
                break;
            case 2:
                print("[ | ] " + "Loading" + ".");
                break;
            case 3:
                print("[ / ] " + "Loading" + "..");
                break;
            default:
                anim = 0;
                print("[ - ] " + "Loading" + "...");
        }
        anim++;
    }

    @Override
    public void run() {
        while(!(main.loadingDone)){
            animate();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Thread " +  threadName + " exiting.");
    }

    public void start () {

        if (t == null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}
