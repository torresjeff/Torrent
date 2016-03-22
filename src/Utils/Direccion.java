/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.Serializable;

/**
 *
 * @author manuela
 */
public class Direccion implements Serializable
{
    public Direccion(String ip, int puerto)
    {
        this.ip = ip;
        this.puerto = puerto;
    }
    
    public String ip;
    public int puerto;

    @Override
    public String toString() {
        return "Direccion{" + "ip=" + ip + ", puerto=" + puerto + '}';
    }
    
    
}
