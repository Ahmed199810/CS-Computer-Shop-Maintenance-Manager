/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Ahmed
 */
public class Permition{
        int id;
        String perm;

        public Permition(int id, String perm) {
            this.id = id;
            this.perm = perm;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPerm() {
            return perm;
        }

        public void setPerm(String perm) {
            this.perm = perm;
        }
        
        
    }