/** 

* @author  Donghao.F 

* @date 2021 Jul 12 11:24:00 

* 

*/
public class ProcInvJNI {
   static {
      System.out.println(System.getProperty("java.library.path"));
      System.loadLibrary("jman");
   }

   public ProcInvJNI() {
   }

   public static native int proc_init(String var0, String var1, String var2);

   public static native int proc_exit(int var0);

   public static void main(String[] args) {
         int ret = proc_init("realtime", "public", "test_proc_init4");
         if (ret < 0)
         {
            System.out.println("fail\n");
            return;
         }
         while (true)
         {
            try {
               Thread.sleep(5);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
   }
}
