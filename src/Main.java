public class Main {
    public static void main(String[] args) {
        BuddySystemAlgorithm bsa = new BuddySystemAlgorithm(2048);
        bsa.allocateRequest("A",20);
        bsa.allocateRequest("B",35);
        bsa.allocateRequest("C",90);
        bsa.allocateRequest("D",40);
        bsa.allocateRequest("E",240);
        bsa.allocateRequest("D",-40);
        bsa.allocateRequest("A",-20);
        bsa.allocateRequest("C",-90);
        bsa.allocateRequest("B",-35);
        bsa.allocateRequest("E",-240);


    }
}
