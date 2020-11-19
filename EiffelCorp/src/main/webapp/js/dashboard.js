

	function msgAddedToWaitingLst() {
	  alert("This car is not available.\n You're on the list.");
	}
	
	
	var button = document.getElementById("addcart")
	var output = document.getElementById("lblCartCount")
	var number
	var counter
	 
	
	function init() {
	  number = parseInt(output.innerText)
	  counter = number + 1
	  
	  function increment() {
	    value = counter++
	    output.innerText = value
	  }
	  button.onclick = increment
	}
	
	window.onload = init  
