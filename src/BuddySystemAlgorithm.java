import java.util.ArrayList;
import java.io.PrintStream;
public class BuddySystemAlgorithm {

    //creating arrayList to store the nodes for different memory sizes
    private ArrayList<MemoryLocation> memoryLocations = new ArrayList<>();
    private int index;
    private static int out=0;


    //constructor for initialization
    public BuddySystemAlgorithm(long size) {
        memoryLocations.add(new MemoryLocation(size));
    }

    //wrapper method for allocateRequest which will be called by main procedure
    public boolean allocateRequest(String process, long allocation) {
        try {
            //storing the allocation value for output purpose.
            long allocationParameter = allocation;
            index = 0;
            boolean returnFlag;
            //getting the first node into variable
            MemoryLocation location = memoryLocations.get(index);
            //if allocation is a request for allocation calling helper allocatePrivate method
            if (allocation > 0) {
                returnFlag = allocatePrivate(process, allocation, location);
            }
            //if its a free memory request then calling helper requestPrivate method
            else {
                allocation = -1 * allocation;
                int locationIndex = 0;
                //checking whether the allocation exist or not
                for (MemoryLocation mem : memoryLocations) {
                    if (mem.allocation == allocation && mem.process.equals(process)) {
                        break;
                    }
                    locationIndex++;
                }
                //if allocation is not present in memory return false;
                if (locationIndex >= memoryLocations.size()) {
                    System.out.println();
                    System.out.print("Allocation size is not valid for " + process + allocationParameter);
                    return false;
                }
                //otherwise free the node for that allocation
                memoryLocations.get(locationIndex).flag = false;
                memoryLocations.get(locationIndex).allocation = 0;
                memoryLocations.get(locationIndex).process = "";
                //retrieving free node details into variable
                location = memoryLocations.get(locationIndex);
                //calling the helper method requestPRivate to check if any other free nodes are available which is equal to current free node
                returnFlag = requestPrivate(location, locationIndex);
            }

            //if helper method returns true printing the output.
            if (returnFlag) {
                System.out.println();
                System.out.print(process + allocationParameter + "   ");
                for (MemoryLocation mem : memoryLocations) {
                    String process1 = mem.process == null ? "" : mem.process;
                    System.out.print(" | " + process1 + " " + mem.size);
                }
                System.out.print("|");
            } else {
                System.out.println();
                System.out.print("Allocation size is not valid for " + process + allocationParameter);
            }

            return returnFlag;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //helper method for handling allocation request
    private boolean allocatePrivate(String process, long allocation, MemoryLocation location) {
        boolean flag;
        long newAllocation;
        //appeding 1 to first child node
        String node1 = location.node + "1";
        //appending 0 to the second child to form a pair once they get free
        String node2 = location.node + "0";
        //if current node is occupied
        if (location.flag) {
            //moving to the next node
            index++;
            //checking the size if it is greater than or equal to the arraylist size
            if (index >= memoryLocations.size())
                //if yes return false.
                return false;
            //if not then recursively calling the same method to check for next node.
            flag = allocatePrivate(process, allocation, memoryLocations.get(index));
            return flag;
        }
        //if the allocation is greater than the memory size
        if (allocation > location.size) {
            //moving to next node
            index++;
            //checking the size if it is greater than or equal to the arraylist size
            if (index >= memoryLocations.size())
                return false;
            //if not recursively calling the same method for next node
            flag = allocatePrivate(process, allocation, memoryLocations.get(index));
            return flag;
        }
        //if allocation is not greater than the memory size
        else {
            //then dividing the memory size into two halves
            newAllocation = location.size / 2;
            //if the half memory size is less than allocation assigning the process to current node
            if (newAllocation < allocation) {
                memoryLocations.get(index).flag = true;
                memoryLocations.get(index).allocation = allocation;
                memoryLocations.get(index).process = process;
                return true;
            }
            //if allocation is not greater than hald memory size
            else {
                //then checking if same memory size is already available in the memory
                for (MemoryLocation mem : memoryLocations) {
                    //if availabe then recursively calling the same method for availabe node
                    if (mem.size == newAllocation && !mem.flag) {
                        index = memoryLocations.indexOf(mem);
                        flag = allocatePrivate(process, allocation, mem);
                        return flag;
                    }
                }
                //if no other node of same size is available in memory
                //checking if the half memory size is greater than allocation and half of half memory size is less than allocation
                //create two new nodes of half size of the current node and assign process to the first node.
                if (newAllocation > allocation && newAllocation / 2 < allocation) {
                    memoryLocations.remove(index);
                    memoryLocations.add(index, new MemoryLocation(newAllocation, node1));
                    memoryLocations.get(index).flag = true;
                    memoryLocations.get(index).allocation = allocation;
                    memoryLocations.get(index).process = process;
                    memoryLocations.add(index + 1, new MemoryLocation(newAllocation, node2));
                    return true;
                }
                //if not then create two new nodes of half size of the current node and recursively call the same method for first node.
                else {
                    memoryLocations.remove(index);
                    memoryLocations.add(index, new MemoryLocation(newAllocation, node1));
                    memoryLocations.add(index + 1, new MemoryLocation(newAllocation, node2));
                    flag = allocatePrivate(process, allocation, memoryLocations.get(index));
                    return flag;
                }
            }
        }
    }

    //helper method for free memory request
    private boolean requestPrivate(MemoryLocation location, int locationIndex) {
        boolean flag;
        long size = location.size;
        //getting the length of the node variable to check whether two nodes belong to the same pair
        int lengthNode = location.node.length();
        //getting the last char of the node to make 1,0 pair
        char lastChar = location.node.charAt(lengthNode - 1);
        //creating the variable for the new node by removing the last char
        String newNode = location.node.substring(0, lengthNode - 1);


        //if the last character of the node variable is zero then checking left  side to make 1,0 pair
        if (lastChar == '0') {
            //if yes then checking if the index is not less than zero
            if (!(locationIndex - 1 < 0)) {
                // getitng the details of the previous nodes
                MemoryLocation previousNode = memoryLocations.get(locationIndex - 1);
                //checking if the previous node is free, node size is same and correct pair of the current node
                if (!(previousNode.flag) && size == previousNode.size &&
                        lengthNode == previousNode.node.length() &&
                        previousNode.node.charAt(lengthNode - 1) == '1') {
                    //if correct pair then deleting two nodes and creating one node of size*2 size
                    memoryLocations.remove(locationIndex);
                    memoryLocations.remove(locationIndex - 1);
                    memoryLocations.add(locationIndex - 1, new MemoryLocation(size * 2, newNode));
                    //recursively calling the same method for newly formed node
                    requestPrivate(memoryLocations.get(locationIndex - 1), locationIndex - 1);
                }
            }
        }
        //if the last character is 1 checking the right side node to make 1,0 pair
        else {
            //if the current node is the last node return true
            if (locationIndex + 1 >= memoryLocations.size()) {
                return true;
            }
            //getting the details of the next node
            MemoryLocation nextNode = memoryLocations.get(locationIndex + 1);
            //if the next node is occupied return true
            if (memoryLocations.get(locationIndex + 1).flag) {
                return true;
            }
            //if next node is free, then checking if is the correct pair
            if (size == memoryLocations.get(locationIndex + 1).size &&
                    lengthNode == nextNode.node.length() &&
                    nextNode.node.charAt(lengthNode - 1) == '0') {
                //if yes then deleting two nodes and creating one node of size*2 size
                memoryLocations.remove(locationIndex + 1);
                memoryLocations.remove(locationIndex);
                memoryLocations.add(locationIndex, new MemoryLocation(size * 2, newNode));
                //recursively  calling the same method for newly formed node.
                requestPrivate(memoryLocations.get(locationIndex), locationIndex);
            }
        }
        return true;
    }

}
