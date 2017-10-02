
print("ColorEffect_v_1")
print("30", flush=True)

#init
nleds = int(input(""))
args = input();
positions = input().split(":");

#update loop
run = True;

while run:
	updateArgs = input().split(":");
	
	if (updateArgs[0] == "q"):
		run = False;
	else:
		time = int(updateArgs[1]);
		step = int(updateArgs[2]);
		
		print("rgb", end='');
		
		for x in range(0, nleds):
			print(":{0}:{1}:{2}".format(step%256, step%256, step%256), end='');
			
		print(flush=True);

