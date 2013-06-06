ANIM = (function() {
	
	var shootDino = {
		width: 120,
		scale: -1,
		img: "img/shoot/char10.png",
        shoot: [{
          x: 0,
          y: 0,
          width: 120,
          height: 90
        }, {
          x: 130,
          y: 0,
          width: 120,
          height: 90
        }, {
          x: 260,
          y: 0,
          width: 120,
          height: 90
        }, {
          x: 390,
          y: 0,
          width: 120,
          height: 90
        }],
        punch: [{
          x: 2,
          y: 138,
          width: 74,
          height: 122
        }, {
          x: 76,
          y: 138,
          width: 84,
          height: 122
        }, {
          x: 346,
          y: 138,
          width: 120,
          height: 122
        }]
      };
      
      var shootCheetah = {
		width: 120,
		scale: -1,
		img: "img/shoot/cheetah.png",
        shoot: [{
          x: 0,
          y: 0,
          width: 120,
          height: 90
        }, {
          x: 130,
          y: 0,
          width: 120,
          height: 90
        }, {
          x: 260,
          y: 0,
          width: 120,
          height: 90
        }, {
          x: 390,
          y: 0,
          width: 120,
          height: 90
        }],
        punch: [{
          x: 2,
          y: 138,
          width: 74,
          height: 122
        }, {
          x: 76,
          y: 138,
          width: 84,
          height: 122
        }, {
          x: 346,
          y: 138,
          width: 120,
          height: 122
        }]
      };
      
	return {
		shootDino : function(){
			return shootDino;
		}, 
		shootCheetah : function(){
			return shootCheetah;
		}
	};

})();