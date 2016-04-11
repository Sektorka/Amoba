package amoba;

public class PasswordGenerator {
    private boolean bUpperCase, bLowerCase, bNumbers, bSpecChars;
    private int iPasswordLength;
    
    public PasswordGenerator(boolean bUpperCase, boolean bLowerCase, 
            boolean bNumbers, boolean bSpecChars, int iPasswordLength){
        this.bUpperCase      = bUpperCase;
        this.bLowerCase      = bLowerCase;
        this.bNumbers        = bNumbers;
        this.bSpecChars      = bSpecChars;
        this.iPasswordLength = iPasswordLength;
    }
    
    public String GeneratePassword(){
        String password = "";
        boolean generateDone = false;
    
        if(!this.bUpperCase && !this.bLowerCase
                && !this.bNumbers && !this.bSpecChars)
            return password;
    
        for(int i = 0; i < this.iPasswordLength; i++){
            do{
                generateDone = false;
                switch(RandomNumber(0,3)){
                    case 0:
                        if(this.bUpperCase){
                            password += this.GenerateUpperCase();
                            generateDone = true;
                        }
                        break;
                    case 1:
                        if(this.bLowerCase){
                            password += this.GenerateLowerCase();
                            generateDone = true;
                        }
                        break;
                    case 2:
                        if(this.bNumbers){
                            password += this.GenerateNumber();
                            generateDone = true;
                        }
                        break;
                    case 3:
                        if(this.bSpecChars){
                            password += this.GenerateSpecChar();
                            generateDone = true;
                        }
                        break;
                }
            }
            while(!generateDone);
        }
    
        return password;
    }
    
    private int GenerateNumber(){
        return RandomNumber(0,9);
    }
    
    private String GenerateUpperCase(){
        String AZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int rand = RandomNumber(0,AZ.length()-1);
        return AZ.substring(rand,rand+1);
    }
    
    private String GenerateLowerCase(){
        String az = "abcdefghijklmnopqrstuvwxyz";
        int rand = RandomNumber(0,az.length()-1);
        return az.substring(rand,rand+1);
    }
    
    private String GenerateSpecChar(){
        String chars = "!'#%&\"()*+`-.{/:;<=>?@[]\\^_|}";
        int rand = RandomNumber(0,chars.length()-1);
        return chars.substring(rand,rand+1);
    }
    
    private int RandomNumber(int iMin, int iMax){
        return (int)(Math.random()*(iMax-iMin+1)+iMin);
    }
}
