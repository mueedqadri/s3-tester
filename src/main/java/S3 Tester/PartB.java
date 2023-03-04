public class PartB {
    public static void main(String args[]){
        S3Uploader s3 = new S3Uploader("serverlessdalnew",
                "Assignment1",
                "Mueed.txt",
                "Mueed.txt");
        s3.start();
    }
}
