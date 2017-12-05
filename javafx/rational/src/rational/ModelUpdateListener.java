/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rational;

import org.json.JSONObject;

/**
 *
 * @author james
 */
@FunctionalInterface
public interface ModelUpdateListener {

    /**
     * The void callback
     * @param updateInfo The callback json data
     */
    void callback(JSONObject updateInfo);

}
