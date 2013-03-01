//testing interuption
//http://www.instructables.com/id/Arduino-Timer-Interrupts/
//

//

//


#define FLOPPY_COUNT 6
#define MAXFRAMECOUNT 32

#define LOWLEVEL __attribute__((always_inline))
#define MINFLOPPYPOS 20
#define MAXFLOPPYPOS 21
// inline
//__attribute__((always_inline))
byte floppydrived = 0;

struct Frame {
  unsigned int frameend;
  unsigned int framehalfperiod;
};

//utility variable for serial transmission
 int inFloppyID = 0;
 unsigned int inframeend = 0;
 unsigned int inframehalfperiod = 0;
 unsigned int inframeendH = 0;
 unsigned int inframeendL = 0;
 unsigned int durationheavy = 0;
 unsigned int durationLight = 0;
//FRAME MANAGEMENT VARIABLES

 int frameplayP[FLOPPY_COUNT] ;
 int frameHeadP[FLOPPY_COUNT] ;

 Frame frameList[FLOPPY_COUNT][MAXFRAMECOUNT];

 unsigned int currentframestep = 0;

// lower engine variables


 unsigned int halfPeriod[FLOPPY_COUNT]  ;
 unsigned int floppyNextStop[FLOPPY_COUNT]  ;
 
 
 unsigned int floppyCounter[FLOPPY_COUNT]  ;
 byte directionPIN[FLOPPY_COUNT] = {
  3,5,7,9,11,13};
 byte motorStepPIN[FLOPPY_COUNT] = {
  2,4,6,8,10,12};

 byte direction[FLOPPY_COUNT]  ;
 unsigned int pos[FLOPPY_COUNT]  ;
/****
BASIC INIT FUNCTION

*************/
LOWLEVEL void doStep(byte drived){

  switchPin(drived);

  if(direction[drived] == 0){
    pos[drived] -=1;
  }
  else{
    pos[drived] += 1;
  }
  delayMicroseconds(2000);
  switchPin(drived);
  delayMicroseconds(2000);
}

LOWLEVEL void doNStep(byte drived,byte n){
   for(int i = 0;i<n;i++){
      doStep(drived);
    }
  
}



/**************************

LOWER ENGINE FUNCTION


*****************************/

LOWLEVEL void goForward(byte drived){
  direction[drived] = 1;
  digitalWrite(directionPIN[drived], LOW);
}
LOWLEVEL void goBackward(byte drived){
  direction[drived] = 0;
  digitalWrite(directionPIN[drived], HIGH);
}
LOWLEVEL void switchDirectionPin(byte drived){
  if(direction[drived] == 0){
    goForward(drived);
  }
  else{
    goBackward(drived);
  }
}

LOWLEVEL byte pinState(byte drived){
  return digitalRead(motorStepPIN[drived]);
}


LOWLEVEL void switchPin(byte drived){
  if(pinState(drived) >=1){
    digitalWrite(motorStepPIN[drived], LOW);
    
    /**
    //IF im going forward, then pos + 1
    //else pos -1
    //changeDirection if position < 20 or > 50
       if(pos[drived] > 30 || pos[drived] < 20 ){
          switchDirectionPin(drived);
       } 
    if( direction[drived] == 1 ){
      pos[drived] +=1;
    }
    else{
      pos[drived] -=1;
    }
    */
  }
  else{
    digitalWrite(motorStepPIN[drived], HIGH);
  }
}

LOWLEVEL void checkFloppy(byte drived,unsigned int counterIncrement){

  //if half period is not 0
  if(halfPeriod[drived] !=0){
    //if it is time to switch the pin
    if(floppyCounter[drived] > floppyNextStop[drived]){
      //switch the pin
      
      //init Counter
      floppyCounter[drived] = 0;
      
      //do a switch pin
      switchPin(drived);
      
      //if this is a step
      if(pinState(drived) < 1){
        
        switchDirectionPin(drived);
        
        // use the code under to make floppy moving
         /*
        //String loge = String();
        if( direction[drived] == 1 ){
          pos[drived] +=1;
          //loge += String("direction is 1 and pos")+pos[drived];
          if(pos[drived] > MAXFLOPPYPOS){
            switchDirectionPin(drived);
          }
        }else{
          //loge += String("direction is 0 and pos")+pos[drived];
          pos[drived] -=1;
          if(pos[drived] < MINFLOPPYPOS){
            switchDirectionPin(drived);
          }
        }
        //Serial.println(loge);
        */
      }
        
     
     //next Stop is the halfPeriod
     floppyNextStop[drived] = halfPeriod[drived];
       
    }else{
      //if too early for switch the pin, then increase counter
      floppyCounter[drived] = floppyCounter[drived] + counterIncrement;
    }
  }
}



/**********************

FRAME MANAGEMENT AREA



***********************/
//update the played frame
LOWLEVEL void updatePlayedFrame(byte floppydrived){
  //TODO do the update only if the currentframestep has changed from this floppy POV
  
  
  // if current played frame ending point reached
  if( frameList[floppydrived][frameplayP[floppydrived]].frameend < currentframestep){
    
    //is there a next frame ?
    int nextFrameP = (frameplayP[floppydrived]+1) % MAXFRAMECOUNT;
    
    //if end time of nextFrame is greated than endtime of currentFrame
    if(frameList[floppydrived][nextFrameP].frameend > frameList[floppydrived][frameplayP[floppydrived]].frameend){
      //go to the next Frame
      frameplayP[floppydrived] = nextFrameP;
      //and play it
      halfPeriod[floppydrived] = frameList[floppydrived][frameplayP[floppydrived]].framehalfperiod;
    }else{
      
      //play the current Frame
      halfPeriod[floppydrived] = frameList[floppydrived][frameplayP[floppydrived]].framehalfperiod;
    }
  }else{
      
      //play the current Frame
      halfPeriod[floppydrived] = frameList[floppydrived][frameplayP[floppydrived]].framehalfperiod;
    }
}

/**
append the frame to the head of the list
*/
LOWLEVEL void addFrameTo(byte floppyID,unsigned int frameEND,unsigned int frameHP){
  int currentHead = frameHeadP[floppyID] ;

  int nextHead = (currentHead+1) % MAXFRAMECOUNT;
  
  frameList[floppyID][nextHead].frameend = frameEND;
  frameList[floppyID][nextHead].framehalfperiod = frameHP;
  
  frameHeadP[floppyID] = nextHead;
}





/****************************

      SETUP




**********************/
  
void setup(){  
  Serial.begin(115200);  
  Serial.println("BEGIN");
  establishContact();
  //Serial.println("  GO!");
  
  for(int drived =  0; drived < FLOPPY_COUNT ; ++drived){
    //initiating all those array to 00000000;
    frameplayP[drived] = 0;
    frameHeadP[drived] = 0;
    halfPeriod[drived] = 0;
    floppyNextStop[drived] = 0 ;
    floppyCounter[drived] = 0;
    direction[drived] = 0;
    pos[drived] = 0;

    pinMode(motorStepPIN[drived], OUTPUT); 
    pinMode(directionPIN[drived], OUTPUT);
    
    digitalWrite(directionPIN[drived], LOW);
    digitalWrite(motorStepPIN[drived], LOW);
    
    goBackward(drived);
    for(int i = 0;i<100;i++){
      doStep(drived);
    }

    goForward(drived);    
    pos[drived] = 0;
    
    
    for(int i = 0;i<20;i++){
      doStep(drived);
    }
  }
  
  //initiate the frame List
  for(int fid = 0 ; fid <FLOPPY_COUNT ; ++fid){
    frameList[fid][0].frameend = 0;
    frameList[fid][0].framehalfperiod = 0;
  }
  
  //set timer1 counter (16bits)
  TCCR1A = 0;// set entire TCCR1A register to 0
  TCCR1B = 0;// same for TCCR1B
  TCNT1  = 0;//initialize counter value to 0
  // set compare match register 
  OCR1A = 65535;//  (must be <65536)
  // turn on CTC mode
  TCCR1B |= (1 << WGM12);
  // Set  bits for prescaler 8
  TCCR1B |=  (1 << CS11);
  
  
  
  
}//end setup

void establishContact() {
  while (Serial.available() <= 0) {
    Serial.println('#');
    delay(300);
  }
  
  while (Serial.available() > 0){
    Serial.read();
  }
}

unsigned int framesubstep = 0 ;
unsigned int counterValue = 0;
unsigned int floppyFrameupdateID = 0;


void loop(){
  
  // get counterValue ;
  // /1 make sound higher freq
  // / 15 make sound lower freq
  counterValue  = TCNT1 ;
  //Serial.print("cv ");  Serial.print(counterValue);  Serial.print("updID ");Serial.print(floppyFrameupdateID); Serial.println();
     
  //reset timer ;
  TCNT1 = 0;
  
  for( floppydrived = 0;floppydrived <FLOPPY_COUNT;++floppydrived){
    //check if I should update floppy output
    checkFloppy(floppydrived, counterValue);
  }
  
  //now checking if data to read, 
  if (Serial.available() >= 3) {
    // get incoming Frame:
    inFloppyID = Serial.read();
    
    durationheavy = Serial.read();
    durationLight = Serial.read();
   
    inframehalfperiod = durationheavy;
    inframehalfperiod = inframehalfperiod << 8;
    inframehalfperiod += durationLight;
    
    //change the freq of this floppy
    if(inFloppyID <= FLOPPY_COUNT){
      halfPeriod[inFloppyID] = inframehalfperiod;
    }else if( inFloppyID == 100 ){
      //inFloppyID == FLOPPY_COUNT
    }

  }
  
}