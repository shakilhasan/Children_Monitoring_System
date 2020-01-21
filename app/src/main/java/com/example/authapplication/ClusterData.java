package com.example.authapplication;

public class ClusterData {
    private String cluster1x;
    private String cluster1y;
    private String numberofpointscluster1;

    private String cluster2x;
    private String cluster2y;
    private String numberofpointscluster2;

    private String cluster3x;
    private String cluster3y;
    private String numberofpointscluster3;

    public ClusterData(){}

    public ClusterData(String cluster1x, String cluster1y, String numberofpointscluster1, String cluster2x, String cluster2y, String numberofpointscluster2, String cluster3x, String cluster3y, String numberofpointscluster3) {
        this.cluster1x = cluster1x;
        this.cluster1y = cluster1y;
        this.numberofpointscluster1 = numberofpointscluster1;
        this.cluster2x = cluster2x;
        this.cluster2y = cluster2y;
        this.numberofpointscluster2 = numberofpointscluster2;
        this.cluster3x = cluster3x;
        this.cluster3y = cluster3y;
        this.numberofpointscluster3 = numberofpointscluster3;
    }

    public String getCluster1x() {
        return cluster1x;
    }

    public String getCluster1y() {
        return cluster1y;
    }

    public String getNumberofpointscluster1() {
        return numberofpointscluster1;
    }

    public String getCluster2x() {
        return cluster2x;
    }

    public String getCluster2y() {
        return cluster2y;
    }

    public String getNumberofpointscluster2() {
        return numberofpointscluster2;
    }

    public String getCluster3x() {
        return cluster3x;
    }

    public String getCluster3y() {
        return cluster3y;
    }

    public String getNumberofpointscluster3() {
        return numberofpointscluster3;
    }
}


