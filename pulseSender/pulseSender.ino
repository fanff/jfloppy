
unsigned int forcedTime = 1200;
unsigned int timeToWaitFactor = 64;

unsigned int nextTimeSwitchT[6] = {0,0,0,0,0,0};
unsigned int timeToWaitT[6] = {0,0,0,0,0,0};

int inByte = 0;         // incoming serial byte

byte directionPIN[6] = {
  1,3,5,7,9,11};
byte motorStepPIN[6] = {
  0,2,4,6,8,10};
byte pinState[6] = {0,0,0,0,0,0};

byte direction[6] = {0,0,0,0,0,0};
unsigned int pos[6] = {0,0,0,0,0,0};


void goForward(byte drived){
  direction[drived] = 1;
  digitalWrite(directionPIN[drived], LOW);
}
void goBackward(byte drived){
  direction[drived] = 0;
  digitalWrite(directionPIN[drived], HIGH);
}


void switchPin(byte drived){
  if(pinState[drived] >=1){
    pinState[drived] = 0;
    digitalWrite(motorStepPIN[drived], LOW);
  }
  else{
    pinState[drived] = 1;
    digitalWrite(motorStepPIN[drived], HIGH);
  }
}
void doStep(byte drived){

  digitalWrite(motorStepPIN[drived], HIGH);

  if(direction[drived] == 0){
    pos[drived] -=1;
  }
  else{
    pos[drived] += 1;
  }
  delayMicroseconds(2000);
  digitalWrite(motorStepPIN[drived], LOW);
  delayMicroseconds(2000);
}

int timeToNextchange(byte key){
  return nextTimeSwitchT[key] - micros();
}



void setup()
{
  Serial.begin(9600);

  digitalWrite(9,LOW);
  digitalWrite(8,LOW);

  
  for(byte drived = 0;drived<=5;drived++){
    pinMode(directionPIN[drived], OUTPUT);
    pinMode(motorStepPIN[drived], OUTPUT);
    
    digitalWrite(directionPIN[drived], LOW);
    digitalWrite(motorStepPIN[drived], LOW);
    
    
    goBackward(drived);
    for(int i = 0;i<100;i++){
      doStep(drived);
    }

    goForward(drived);    
    pos[drived] = 0;
    
    nextTimeSwitchT[drived] = micros()+forcedTime;
  }
  
  //while true do loop
}



void loop()
{

  for(byte drived = 0;drived<=5;drived++){
  
      if(timeToNextchange(drived) < 0){
        if(pinState[drived] >0){
          // doing the off front
          //switch the pin
          switchPin(drived);
          //dont move during forcedTIme+aTimeToWait;
          nextTimeSwitchT[drived] = micros()+forcedTime+timeToWaitT[drived];
    
          if(direction[drived] == 0){
            pos[drived] -=1;
          }
          else{
            pos[drived] += 1;
          }
          if(pos[drived]<= 40){
            goForward(drived);
          }
          if(pos[drived] >=41){
            goBackward(drived);
          }
    
        }
        else{
          if(timeToWaitT[drived]>0){
            //switch the pin
            switchPin(drived);
          }
    
          //dont move during forcedTIme;
          nextTimeSwitchT[drived] = micros()+forcedTime;
        }
      }
      else{
        // if we get a valid byte, read analog ins:
        if (Serial.available() >= 6) {
          // get incoming byte:
          for(byte drived = 0;drived<=5;drived++){
            inByte = Serial.read();
            timeToWaitT[drived] = (unsigned int)((inByte)*timeToWaitFactor);
          }
          
        }
      }
  
  
  }

}





