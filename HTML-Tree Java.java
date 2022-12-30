// Vincent Revel Aldisa_205150200111019
// Muhammad Huda Nugraha 205150201111015
// Muhamad Haikal Fajri_205150207111016

package HTMLProject;
import java.util.Scanner;

public class T2_205150200111019_VincentRevelAldisa {
    public static void main(String[] args) {
        Tree tr = new Tree();
        tr.tree = new Node(null, "html", "html",0);
        Scanner in = new Scanner(System.in);
        int index = 0;
        String [] arrInput = new String[index];
        while(in.hasNextLine()){
            String line = in.nextLine();
            if(line.length() == 0){
                break;
            }else{
                index++;
                String [] oldArray = arrInput;
                arrInput = new String[index];
                for(int i = 0; i < oldArray.length; i++){
                    arrInput[i] = oldArray[i];
                }
                arrInput[index-1] = line;
            }
        }
        for(int i = 0; i < arrInput.length; i++){
            String [] spliter = arrInput[i].split(";");
            if (spliter[0].equals("ADD")){
                if (spliter[1].equals("TEXT")){
                    tr.addText(tr.tree, spliter);
                }else {
                    tr.addNode(tr.tree, spliter);
                }
            }else if (spliter[0].equals("DELETE")){
                tr.searchSelector(tr.tree, spliter[1]);
                Node [] save = tr.getResult();
                tr.removeResult();
                if (save == null){
                    System.out.println("tidak ditemukan " + spliter[1]);
                }else if (save.length == 1){
                    tr.delete(save[0]);
                }
            }else if (spliter[0].equals("PRINT")){
                tr.spec = 0;
                tr.searchSelector(tr.tree, spliter[1]);
                Node [] save = tr.getResult();
                tr.removeResult();
                if (save == null){
                    System.out.println("tidak ditemukan " + spliter[1]);
                }else if (save.length == 1){
                    if (save[0].level > 0){
                        tr.spec = save[0].level;
                    }
                    tr.display(save[0], Integer.parseInt(spliter[2]));
                }else if (save.length > 1){
                    for (int j = 0; j < save.length; j++){
                        tr.spec = 0;
                        if (save[j].level > 0){
                            tr.spec = save[j].level;
                        }
                        tr.display(save[j], Integer.parseInt(spliter[2]));
                    }
                }
            }else if (spliter[0].equals("SEARCH")){
                tr.searchSelector(tr.tree, spliter[1]);
                Node [] save = tr.getResult();
                tr.removeResult();
                if (save == null){
                    System.out.println("tidak ditemukan " + spliter[1]);
                }else if (save.length == 1){
                    Node [] arrPrint = new Node[save[0].level+1];
                    arrPrint[save[0].level] = save[0];
                    for (int j = save[0].level; j > 0; j--){
                        arrPrint[j-1] = arrPrint[j].parent;
                    }
                    for(int j = 0; j < arrPrint.length-1; j++){
                        if (j > 0){
                            System.out.println();
                        }
                        int indexDOT = arrPrint[j].level * 3;
                        for (int k = 0; k < indexDOT; k++) {
                            System.out.print("-");
                        }
                        System.out.printf("<%s id=\"%s\">", arrPrint[j].nameTag, arrPrint[j].id);
                    }
                    System.out.println();
                    int indexDOT = arrPrint[save[0].level].level * 3;
                    for (int k = 0; k < indexDOT; k++) {
                        System.out.print("-");
                    }
                    System.out.printf("<%s id=\"%s\"></%s>", arrPrint[arrPrint.length-1].nameTag, arrPrint[arrPrint.length-1].id, arrPrint[arrPrint.length-1].nameTag);
                    for (int j = arrPrint.length-2; j >= 0; j --){
                        System.out.println();
                        int indexDOTX = arrPrint[j].level * 3;
                        for (int k = 0; k < indexDOTX; k++) {
                            System.out.print("-");
                        }
                        System.out.printf("</%s>", arrPrint[j].nameTag);
                    }
                }
                System.out.println();
            }
        }
    }
}
class Node {
    Node selector;
    Node [] child ;
    Node parent;
    String nameTag;
    String id;
    String text;
    int index = 1;
    int indexTXT = 0;
    int level;

    public Node(int level){
        this.level = level;
    }
    public Node(Node selector, String nameTag, String id, int level){
        this.parent = selector;
        this.nameTag = nameTag;
        this.id = id;
        this.level = level;
    }

    public void insert(Node parent, Node child){
        child.selector = parent;
        if (parent.child == null){
            parent.child = new Node[1];
            parent.child[0] = child;
            return;
        }
        Node [] oldArr = parent.child;
        parent.child = new Node[++index];
        for (int i = 0; i < oldArr.length; i++){
            parent.child[i] = oldArr[i];
        }
        parent.child[index-1] = child;
    }

    public void insertText(Node parent, String text, Node child){
        indexTXT++;
        child.text = text;
        child.nameTag = "TEXT";
        parent.insert(parent, child);
    }
}

class Tree {
    Node tree;
    Node[] result;
    private int indexR = 1;
    int spec = 0;

    public void arrResult(Node data) {
        if (this.result == null) {
            result = new Node[1];
            result[0] = data;
            return;
        }
        Node[] oldArray = this.result;
        result = new Node[indexR + 1];
        indexR++;
        for (int i = 0; i < oldArray.length; i++) {
            result[i] = oldArray[i];
        }
        result[indexR - 1] = data;
    }

    public Node[] getResult() {
        return this.result;
    }

    public void removeResult() {
        this.result = null;
        this.indexR = 1;
    }

    public void searchSelector(Node data, String selector) {
        if (data != null) {
            if (data.nameTag.equals(selector)) {
                arrResult(data);
            }
            if (data.child == null) {
                return;
            }
            for (int j = 0; j < data.child.length; j++) {
                searchSelector(data.child[j], selector);
            }
        }
    }

    public void addNode(Node data, String[] input) {
        Node[] save;
        String parent = input[1];
        String output = parent;
        if (parent.charAt(0) == '#') {
            parent = parent.substring(1);
            searchID(data, parent);
            save = getResult();
            removeResult();
        } else {
            searchSelector(data, parent);
            save = getResult();
            removeResult();
        }
        if (save == null) {
            System.out.println("tidak ditemukan " + output);
        } else if (save.length == 1) {
            searchID(tree, input[3]);
            if (result != null) {
                System.out.println(input[3] + " sudah ada");
            } else {
                tree.insert(save[0], new Node(save[0], input[2], input[3], save[0].level + 1));
                System.out.printf("tambah <%s id=\"%s\"> pada %s\n", input[2], input[3], parent);
            }
            removeResult();
        } else if (save.length > 1) {
            for (int j = 0; j < save.length; j++) {
                String newID = input[3] + (j + 1);
                searchID(tree, newID);
                if (result != null) {
                    System.out.println(newID + " sudah ada");
                } else {
                    tree.insert(save[j], new Node(save[j], input[2], newID, save[j].level + 1));
                    System.out.printf("tambah <%s id=\"%s\"> pada %s\n", input[2], newID, save[j].id);
                }
                removeResult();
            }
        }
    }

    public void addText(Node data, String[] input) {
        Node[] save;
        String parent = input[2];
        String output = parent;
        if (parent.charAt(0) == '#') {
            parent = parent.substring(1);
            searchID(data, parent);
            save = getResult();
            removeResult();
        } else {
            searchSelector(data, parent);
            save = getResult();
            removeResult();
        }
        if (save == null) {
            System.out.println("tidak ditemukan " + output);
        } else if (save.length == 1) {
            save[0].insertText(save[0], input[3], new Node(save[0].level + 1));
            System.out.printf("tambah teks \"%s\" pada %s\n", input[3], parent);
        } else if (save.length > 1) {
            for (int j = 0; j < save.length; j++) {
                save[j].insertText(save[0], input[3], new Node(save[j].level + 1));
                System.out.printf("tambah teks \"%s\" pada %s\n", input[3], parent);
            }
        }
    }

    public void searchID(Node data, String iD) {
        if (data != null) {
            if (data.id == null) {
                return;
            }
            if (data.id.equals(iD)) {
                arrResult(data);
            }
            if (data.child == null) {
                return;
            }
            for (int j = 0; j < data.child.length; j++) {
                searchID(data.child[j], iD);
            }
        }
    }

    public void delete(Node data) {
        if (data.child == null) {
            System.out.println("hapus " + data.id);
            data = null;
        } else {
            for (int i = 0; i < data.child.length; i++) {
                delete(data.child[i]);
            }
            System.out.println("hapus " + data.id);
            data = null;
        }
    }

    public void display(Node data, int depthMax) {
        if (data == null) {
            return;
        }
        int indexDOT = (data.level - spec) * 3;
        if (data.level > depthMax) {
            return;
        } else {
            for (int i = 0; i < indexDOT; i++) {
                System.out.print("-");
            }
            System.out.printf("<%s id=\"%s\">", data.nameTag, data.id);
            if (data.child == null) {
                System.out.println();
                for (int i = 0; i < indexDOT; i++) {
                    System.out.print("-");
                }
                System.out.printf("</%s>\n", data.nameTag);
                return;
            } else if (data.child.length == 1) {
                if (data.child[0].level > depthMax) {
                    System.out.println();
                    for (int i = 0; i < indexDOT; i++) {
                        System.out.print("-");
                    }
                    System.out.printf("</%s>\n", data.nameTag);
                    return;
                } else {
                    if (data.child[0].nameTag.equals("TEXT")) {
                        int textIDX = data.child[0].level * 3;
                        System.out.print(data.child[0].text);
                        System.out.printf("</%s>\n", data.nameTag);
                        return;
                    } else {
                        System.out.println();
                        display(data.child[0], depthMax);
                    }
                }
            } else if (data.child.length > 1) {
                System.out.println();
                if (data.child[0].level > depthMax) {
                    for (int i = 0; i < indexDOT; i++) {
                        System.out.print("-");
                    }
                    System.out.printf("</%s>\n", data.nameTag);
                    return;
                }
                for (int i = 0; i < data.child.length; i++) {
                    if (data.child[i] == null) {
                    } else {
                        if (data.child[i].nameTag.equals("TEXT")) {
                            int textIDX = (data.child[i].level - spec) * 3;
                            for (int j = 0; j < textIDX; j++) {
                                System.out.print("-");
                            }
                            System.out.print(data.child[i].text + "\n");
                        } else {
                            display(data.child[i], depthMax);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < indexDOT; i++) {
            System.out.print("-");
        }
        System.out.printf("</%s>\n", data.nameTag);
    }
}
