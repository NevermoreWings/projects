(*
 *  CS164 Spring 2004
 *
 *  Programming Assignment 1
 *    Implementation of a simple stack machine.
 *
 *  Skeleton file
 *)

class Main inherits IO {
	  input : String;
   	  stacker: Stack;
   main() : Object {
   	{
   	  out_string(">");
   	  stacker<-(new Stack).init();
   	  input<- in_string();
   	  while not input="x" 
   	  loop
   	  	{
   	  		if input = "d" then stacker.print() else    
   	  		if input = "e" then                         
   	  			if stacker.value()= "s" then stacker.swap() else
   	  			if stacker.value()= "+" then stacker.add() else 
					0

   	  			fi fi 
   	  		else
   	  		stacker<- (new Stack).set(input, stacker)
   	  		fi fi; 
   	  		out_string("\n");
   	  		out_string(">");
   	  		input<- in_string();
   	  	}

   	  pool;
   }
   };

};
class Stack inherits IO{
	element: String;
	next: Stack;
	temp1: String; 
	temp2: String;
	temp3: Stack;
	temp4: Stack; 
	init(): Stack { 
		{
			element<- "Null";
			self;
		}
	};
	set(i: String, rest: Stack) : Stack 
	{
		{
			element<- i;
			next<- rest;
			self;
		}
	};
	print() : Stack
	{
		{
			
			if not element= "Null" 
			then 
			{out_string("\n"); 
			out_string(element); 
			next.print(); 
			} 
			else 0

			fi; 
			self;
		}
	};
	swap() : Stack
	{
		{
			pop();
			temp1<-element; 
			temp2<- next.value();
			temp3<- next.tail();
			element<- temp2;
			temp4<- (new Stack).set(temp1, temp3);
			element<-temp2;
			next<-temp4;
			self;
		}
	};
	add() : Stack
	{
		{
			pop();
			temp1<-element; 
			temp2<- next.value();
			temp3<- next.tail(); 
			next<- temp3;
			element<- (new A2I).i2a((new A2I).a2i(temp1)+ (new A2I.a2i(temp2)));
			self;
		}
	};
	pop() : Stack
	{
		{
		element<-set_value(next.value());
		next<- set_tail(next.tail());
		self;
		}
	};
	value(): String{element};
	set_value(s: String): String{element<- s};
	tail(): Stack {next}; 
	set_tail(s: Stack): Stack{next<-s};

};

