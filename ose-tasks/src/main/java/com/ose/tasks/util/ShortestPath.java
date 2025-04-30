package com.ose.tasks.util;

import java.util.*;

public class ShortestPath {
//    static int min = Integer.MAX_VALUE;
//    static int[][] edge = new int[100][100];
//    static int[] vertex = new int[100];
//    static int n, m;
//    static Scanner input = new Scanner(System.in);
/*    static int[][] graph = {
    {0,2,Integer.MAX_VALUE,Integer.MAX_VALUE,10},
    {Integer.MAX_VALUE,0,3,Integer.MAX_VALUE,7},
    {4,Integer.MAX_VALUE,0,4,Integer.MAX_VALUE},
    {Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,0,5},
    {Integer.MAX_VALUE,Integer.MAX_VALUE,3,Integer.MAX_VALUE,0}
};*/

    static int M = Integer.MAX_VALUE;
    private static Map<Integer, String> map = new HashMap<>();


    private static Map<Integer, Integer> shortPath = new HashMap<>();

    static int[][] graphx = {
        {0,M,M,M,M,M,M,1,M,1,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//0
        {M,0,M,M,M,M,1,M,1,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//1
        {M,1,0,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,1},//2
        {1,M,1,0,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,1},//3
        {M,1,1,M,0,1,1,1,M,M,M,M,M,M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//4
        {1,M,M,1,1,0,1,1,M,M,M,M,M,M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//5
        {M,M,M,M,1,1,0,1,1,M,1,M,M,M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//6
        {M,M,M,M,1,1,1,0,M,1,M,1,M,M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//7
        {M,M,M,M,M,M,1,M,0,M,1,M,M,1,M,M,M,1,M,1,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//8
        {M,M,M,M,M,M,M,1,M,0,M,1,M,M,1,M,M,M,1,M,1,1,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//9
        {M,M,M,M,M,M,1,M,1,M,0,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M},//10
        {M,M,M,M,M,M,M,1,M,1,1,0,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M},//11
        {M,M,M,M,M,M,M,M,M,M,1,1,0,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M},//12
        {M,M,M,M,M,M,M,M,1,M,M,M,M,0,M,M,M,1,M,1,M,M,1,M,M,M,M,M,M,1,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M},//13
        {M,M,M,M,M,M,M,M,M,1,M,M,M,M,0,M,M,M,1,M,1,1,M,1,M,M,M,M,M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M},//14
        {M,M,M,M,1,1,1,1,M,M,M,M,M,M,M,0,1,1,M,M,M,M,M,M,1,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//15
        {M,M,M,M,1,1,1,1,M,M,M,M,M,M,M,1,0,M,1,M,M,M,M,M,M,1,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//16
        {M,M,M,M,M,M,M,M,1,M,M,M,M,1,M,1,M,0,M,1,M,M,1,M,1,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//17
        {M,M,M,M,M,M,M,M,M,1,M,M,M,M,1,M,1,M,0,M,1,1,M,1,M,1,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//18
        {M,M,M,M,M,M,M,M,1,M,M,M,M,1,M,M,M,1,M,0,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M},//19
        {M,M,M,M,M,M,M,M,M,1,M,M,M,M,1,M,M,M,1,M,0,1,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M},//20
        {M,M,M,M,M,M,M,M,M,1,M,M,M,M,1,M,M,M,1,M,1,0,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M},//21
        {M,M,M,M,M,M,M,M,1,M,M,M,M,1,M,M,M,1,M,1,M,M,0,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M},//22
        {M,M,M,M,M,M,M,M,M,1,M,M,M,M,1,M,M,M,1,M,1,1,M,0,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M},//23
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,1,M,M,M,M,M,M,0,M,1,1,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M},//24
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,1,M,M,M,M,M,M,0,M,M,1,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M},//25
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,1,M,M,M,M,M,M,1,M,0,1,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M},//26
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,1,M,M,M,M,M,M,1,M,1,0,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M},//27
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,1,M,M,M,M,M,M,1,M,M,0,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M},//28
        {M,1,1,M,1,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M},//29
        {1,M,M,1,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,M,M,M,M,M,M,M,M,M,M,M,M,M},//30
        {M,M,M,M,M,M,M,M,M,M,M,M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,1,0,M,M,M,M,M,M,M,M,M,M,M,M,M},//31
        {M,M,M,M,M,M,M,M,M,M,1,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,M,M,M,M,M,M,M,M,M,M,M},//32
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,0,M,M,M,M,M,M,M,M,M,M,M},//33
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,0,M,M,M,M,M,M,M,M,M,M},//34
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,0,M,M,M,M,M,M,M,M,M},//35
        {M,M,M,M,M,M,M,M,M,M,   M,M,M,M,M,M,M,M,M,M,    M,M,M,M,1,M,M,M,M,M,    M,M,M,M,M,M,0,M,M,M,    M,M,M,M,M},//36
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,0,M,M,M,M,M,M,M},//37
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,M,M,M,M,M},//38
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,M,M,M,M},//39
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,M,M,M},//40
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,M,M},//41
        {M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,M,M},//42
        {M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,0,1},//43
        {M,M,1,1,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,M,1,0}//44
    };

    //自定义比较器，降序排列
    static Comparator<Vectorx> cmp = new Comparator<Vectorx>() {
        @Override
        public int compare(Vectorx e1, Vectorx e2) {
            return e2.vectorDist - e1.vectorDist;
        }
    };

    public static void main(String[] args) {

        PriorityQueue<Vectorx> pQueue = new PriorityQueue<Vectorx>(cmp);

        int currentVectorNo = 34;
        int originalNo = currentVectorNo;
        Vectorx vectorx = new Vectorx();
        vectorx.setVectorDist(0);
        vectorx.setVectorNo(currentVectorNo);
        pQueue.add(vectorx);

        //初始化结果队列
        Map<Integer, VectorDistance> vectorDistances = new HashMap<>();
        for(int i = 1; i <= graphx[1].length; i++) {
            VectorDistance vectorDistance = new VectorDistance();
            if(i == currentVectorNo)     {
                vectorDistance.setDistance(0);
                vectorDistance.setPath(0);

            } else {

                vectorDistance.setDistance(Integer.MAX_VALUE);
                vectorDistance.setPath(0);

            }
            vectorDistance.setStartVector(i);
            vectorDistances.put(i, vectorDistance);

        }

        while(true){

            Vectorx vx = pQueue.poll();
            currentVectorNo = vx.getVectorNo();
            for(int k = 1; k <=graphx[1].length;k++) {
                if(k == currentVectorNo) continue;
                if(graphx[currentVectorNo - 1][k - 1] == Integer.MAX_VALUE) continue;
                int vxDist = graphx[currentVectorNo - 1][k - 1] + vx.getVectorDist();
                if(vxDist < vectorDistances.get(k).getDistance()) {
                    vectorDistances.get(k).setPath(currentVectorNo);
                    vectorDistances.get(k).setDistance(vxDist);
                    Vectorx nVectorx = new Vectorx();
                    nVectorx.setVectorNo(k);
                    nVectorx.setVectorDist(vxDist);
                    pQueue.add(nVectorx);
                }


            }


            if(pQueue.isEmpty()) {
                break;
            }
        }

//        for( int i = 1; i < graphx[1].length; i++) {
//            shortPath.put(i, vectorDistances.get(i).getPath());
//        }

        int targetNode = 2;
        int originalNode = targetNode;
        List<Integer> path = new ArrayList<>();
        path.add(targetNode);

        while(true) {
            targetNode = vectorDistances.get(targetNode).getPath();

            if(targetNode == 0) {
//                path.add(originalNo);
                break;
            }

            path.add(targetNode);



        }
        Collections.reverse(path);

        map.put(1,"PA001");
        map.put(2,"PA002");
        map.put(3,"V001");
        map.put(4,"V002");
        map.put(5,"V005");
        map.put(6,"V006");
        map.put(7,"V009");
        map.put(8,"V010");
        map.put(9,"V011");
        map.put(10,"V012");
        map.put(11,"V013");
        map.put(12,"V014");
        map.put(13,"V015");
        map.put(14,"V017");
        map.put(15,"V018");
        map.put(16,"V019");
        map.put(17,"V020");
        map.put(18,"V021");
        map.put(19,"V022");
        map.put(20,"V023");
        map.put(21,"V024");
        map.put(22,"V025");
        map.put(23,"V026");
        map.put(24,"V027");
        map.put(25,"V028");
        map.put(26,"V029");
        map.put(27,"V030");
        map.put(28,"V031");
        map.put(29,"V032");
        map.put(30,"V038");
        map.put(31,"V039");
        map.put(32,"V040");
        map.put(33,"V041");
        map.put(34,"WBT1P");
        map.put(35,"WBT1");
        map.put(36,"WBT1S");
        map.put(37,"WBT2P");
        map.put(38,"WBT2S");
        map.put(39,"WBT3P");
        map.put(40,"WBT3S");
        map.put(41,"WBT4");
        map.put(42,"WBT4P");
        map.put(43,"WBT4S");
        map.put(44,"SCP");
        map.put(45,"SCS");

        System.out.println("From start Point " + map.get(originalNo) + " to Target node " + map.get(originalNode) + " Path: \n");

        path.forEach(p ->{
            System.out.println(" " + map.get(p) + "\n");
        });

//        for( int i = 1; i < graphx[1].length; i++) {
//            System.out.println("Vector " + i + " minimal Distance is: " + vectorDistances.get(i).getDistance() + " , path is Vector" + vectorDistances.get(i).getPath());
//        }
    }

    public static class Vectorx{
        private int vectorNo;

        private int vectorDist;

        public int getVectorNo() {
            return vectorNo;
        }

        public void setVectorNo(int vectorNo) {
            this.vectorNo = vectorNo;
        }

        public int getVectorDist() {
            return vectorDist;
        }

        public void setVectorDist(int vectorDist) {
            this.vectorDist = vectorDist;
        }
    }

    public static class VectorDistance {
        private int startVector;

        private int distance;

        private int path;

        public int getStartVector() {
            return startVector;
        }

        public void setStartVector(int startVector) {
            this.startVector = startVector;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getPath() {
            return path;
        }

        public void setPath(int path) {
            this.path = path;
        }
    }

}
