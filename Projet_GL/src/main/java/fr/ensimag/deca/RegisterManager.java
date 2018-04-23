package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl17 Anais & Lucas
 * @date 01/01/2018
 */
public class RegisterManager {
      private int registerMax;
      private int registerCourant;
      // private boolean maxAtteint = false;
      private int maxTempo = 2;
      private int varGlobale =2;


      private int regDebutBloc = 0;
      private int regFinBloc = regDebutBloc;
      private int varLocale = 0;

      public RegisterManager(int registerMax, int registerCourant){
        this.registerMax = registerMax;
        this.registerCourant = registerCourant;
      }

      public int getRegisterMax(){
        return this.registerMax;
      }

      public int getRegisterCourant(){
        return this.registerCourant;
      }

      public void setRegisterMax(int rMax){
        this.registerMax = rMax;
      }

      public void decrementRegisterCourant(){
        this.registerCourant--;
      }

      public void incrementCompteurCourant(){
    	if (!this.getMaxAtteint()) {
    		this.registerCourant ++;
    	}
      }

      public boolean getMaxAtteint(){
          return (this.registerMax == this.registerCourant);
      }


      public boolean verify(int nbPlaceNecessaire){
        if ((this.registerMax - this.registerCourant)>= nbPlaceNecessaire){
          return true;
        }
        else{
          return false;
        }
      }

      public int getMaxTempo(){
          return this.maxTempo;
      }

      public int getVarGlobale(){
          return this.varGlobale;
      }

      public int getVarLocale(){
          return this.varLocale;
      }

      public void incrementMaxTempo(){
          this.maxTempo++;
      }

      public void incrementVarGlobale(){
          this.varGlobale++;
      }

      public void incrementVarLocale(){
          this.varLocale++;
      }

      public void checkMax(){
          if(this.registerCourant > this.maxTempo){
              this.maxTempo = this.registerCourant;
          }
      }

      public void setRegDebut(){
          this.regDebutBloc = this.registerCourant;
          this.regFinBloc = this.registerCourant;
      }

      public void setVarLocale(){
          this.varLocale=0;
      }

      public void checkRegFin(){
        if(this.registerCourant > this.regFinBloc){
            this.regFinBloc = this.registerCourant;
        }
      }

      public int getRegDebutBloc(){
          return this.regDebutBloc;
      }

      public int getRegFinBloc(){
          return this.regFinBloc;
      }


}
