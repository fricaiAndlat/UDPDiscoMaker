
print("ColorEffect_v_1")
print("30")

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
		time = updateArgs[1];
		step = updateArgs[2];
		
		print("rgb");
		
		for x in range(0, nleds):
			print(":{0}:{1}:{2}".format(step%256, step%256, step%256));

