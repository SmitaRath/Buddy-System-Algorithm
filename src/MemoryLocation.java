//class to store the memory location details
public class MemoryLocation {
    //process name which will acquire the memory
    String process;
    //size of the memory
    long size;
    //flag to check if it is occupied or not
    boolean flag;
    //allocation size of the process which is accoupied this memormy
    long allocation;
    //field to keep track of the parent and child node, to form pair of 1 and 0
    String node;

    //constructor to initializa the parameters
    public MemoryLocation(long size){
        this.size=size;
        this.flag=false;
        this.process="";
        //root node is assigned 1
        node="1";
        //to display the total size of the memory
        System.out.print("|                " + size + "                                                     |");
    }

    //second constructor to create a node with size and node.
    public MemoryLocation(long size,String node){
        this.size=size;
        this.flag=false;
        this.node=node;

    }
}
