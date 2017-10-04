        function effect_queue_darude() {

			effectQueue = {
				"layer": [
					{
						"effects": [
							{
							"name": "colorblink",
							"length": 3600,
							"fade": 2400,
							"cycleLen": 443,
							"args": "red,blue,yellow,green",
							"scaleX": 500,
							"scaleY": 1000,
							"scaleZ": 1
							},
							{
							"name": "colorblink",
							"length": 99999999,
							"fade": 0,
							"cycleLen": 99999999,
							"args": "black",
							"scaleX": 99999999,
							"scaleY": 99999999,
							"scaleZ": 1
							}
						]
					}
				]
			};
			effect("EffectQueue", JSON.stringify(effectQueue));

			soundboard("darude.wav");
		}

		function effect_queue_runninginthe90s() {

			effectQueue = {
				"layer": [
					{
						"effects": [
							{
								"name": "none",
								"length": 0,
								"cycleLen": 0,
								"fade": 750,

							},
							{
								"name": "colorblink",
								"length": 1450,
								"cycleLen": 1500,
								"fade": 0,
								"args": "white",
								"scaleX": 750,
								"scaleY": 700,
								"scaleZ": 1

							},
							{
								"name": "colorblink",
								"length": 5000,
								"fade": 1300,
								"cycleLen": 385,
								"args": "red,green,blue,violet,yellow,cyan,orange",
								"scaleX": 400,
								"scaleY": 1000,
								"scaleZ": 1
							},
							{
								"name": "colorblink",
								"length": 99999999,
								"fade": 0,
								"cycleLen": 99999999,
								"args": "black",
								"scaleX": 99999999,
								"scaleY": 99999999,
								"scaleZ": 1
							}
						]
					},
					{
						"effects": [
							{
								"name": "none",
								"length": 1450,
								"cycleLen": 1450,
								"fade": 0,

							},
							{
								"name": "flashing",
								"length": 5000,
								"fade": 0,
								"cycleLen": 385,
								"scaleX": 100,
								"onW": 1

							},
							{
								"name": "colorblink",
								"length": 99999999,
								"fade": 0,
								"cycleLen": 99999999,
								"args": "black",
								"scaleX": 99999999,
								"scaleY": 99999999,
								"scaleZ": 1
							}
						]
					}
				]
			};
			effect("EffectQueue", JSON.stringify(effectQueue));

			soundboard("runninginthe90s.wav");
		}

		function soundboard(filename) {
			var xmlhttp = new XMLHttpRequest();
			var url = "http://10.0.0.10:8080/play/" + filename;

			xmlhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					console.log("enqued: " + filename);
				}
			};
			xmlhttp.open("GET", url, true);
			xmlhttp.send();
		}