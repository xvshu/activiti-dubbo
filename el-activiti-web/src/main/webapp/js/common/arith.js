/**
 * <p>Title: arith</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: creditcities</p>
 * @author mshi
 * @version 1.0
 * @function list
 * 1.Arith.add
 * 2.Arith.sub
 * 3.Arith.mul
 * 4.Arith.div
 * 5.Arith.calc
 * 6.Arith.quickAverage
 * 7.Arith.max
 * 8.Arith.min
 */

/*
 * Arith functions
 * These functions are used to add, subtract, multiply, and divide etc. number objects.
 * must include format.js file
 */
var Arith = {
	version : "1.0"
};

/*
 * add computation
 * @param support for multiple numbers or multiple objects
 * @return number
 */
Arith.add = function () {
	var data = 0;
	if (arguments.length > 0) {
		for (var i = 0; i< arguments.length; i++) {
			if (typeof(arguments[i]) == 'object') arguments[i] = arguments[i].value;
			data = this.calc(data, arguments[i], "+");
		}
	}
	return Number(data);
};

/*
 * subtration computation
 * @param support for multiple numbers or multiple objects
 * @return number
 */
Arith.sub = function () {
	var data = 0;
	if (arguments.length > 0) {
		if (typeof(arguments[0]) == 'object') arguments[0] = arguments[0].value;
		data = arguments[0];
		for (var i = 1; i< arguments.length; i++) {
			if (typeof(arguments[i]) == 'object') arguments[i] = arguments[i].value;
			data = this.calc(data, arguments[i], "-");
		}
	}
	return Number(data);
};

/*
 * multiplication computation
 * @param support for multiple numbers or multiple objects
 * @return number
 */
Arith.mul = function () {
	var data = 1;
	if (arguments.length > 0) {
		for (var i = 0; i< arguments.length; i++) {
			if (typeof(arguments[i]) == 'object') arguments[i] = arguments[i].value;
			data = this.calc(data, arguments[i], "*");
		}
	}
	return Number(data);
};

/*
 * dividing computation
 * @param support for multiple numbers or multiple objects
 * @return number
 */
Arith.div = function () {
	var data = 0;
	if (arguments.length > 0) {
		if (typeof(arguments[0]) == 'object') arguments[0] = arguments[0].value;
		data = arguments[0];
		for (var i = 1; i< arguments.length; i++) {
			if (typeof(arguments[i]) == 'object') arguments[i] = arguments[i].value;
			data = this.calc(data, arguments[i], "/");
		}
	}
	return Number(data);
};
Arith.roundNum = function(number, digit) {
	    var myOldAmt = "0";
	    if(isNaN(number)) {
	  	    return roundNum(myOldAmt, digit);
	    }
	    if(isNaN(digit)) {
	        return myOldAmt;
	    }
	    var num = number;
	    var iDig = (typeof(digit) == "string") ? Number(digit) : digit;
	    num = (typeof(num) == "string") ? Number(this.removeLeadingZero(num)) : num;
	    var newNum = Math.round(num * Math.pow(10, iDig)) / (Math.pow(10, iDig) * 1.0);
	    newNum = Number(newNum).toFixed(iDig);
	    return (isNaN(newNum)) ? num : newNum;
};
/*
 * mathematical computation
 * @param num1 support numbers
 * @param num2 support numbers
 * @param type the operator: '+', '-', '*', '/'
 * @return number
 */
Arith.calc = function (num1, num2, type){
	this.convertBigNumberStrExpress = function(numStr) {
		var indexTmp1 = numStr.indexOf("e");
		var indexTmp2 = numStr.indexOf("E");
		if(indexTmp1 != -1 || indexTmp2 != -1){
			if(indexTmp1 != -1){
				return this.combinPrefixAndSubfix(numStr, indexTmp1);
			} else if(indexTmp2 != -1){
				return this.combinPrefixAndSubfix(numStr, indexTmp2);
			}
		} else {
			return numStr;
		}	
	};
	this.combinPrefixAndSubfix = function(numStr, eIndex) {
		var front = numStr.substring(0,eIndex);
		var scale = parseInt(numStr.substring(eIndex+2));
		var opeType = numStr.substring(eIndex+1,eIndex+2);
		var resultStr = "";
	    var elmValues = front.split('.');
	    //get integer string. number before dot.
	    var integerNumber = elmValues[0];
	    //get decimal digits string. number after dot.
	    var decimalDigits = elmValues[1];
	    if(decimalDigits == undefined)
	 	   decimalDigits = "";
		if(opeType == '+'){
			var offSet = scale - decimalDigits.length;
			resultStr = integerNumber + this.offSetNumberStrWhenAdd(decimalDigits, offSet);
		} else if(opeType == '-'){
			resultStr = this.offSetNumberStrWhenSub(integerNumber, scale) + decimalDigits;			
		}
		return (resultStr);
	};
	this.offSetNumberStrWhenAdd = function(intStr, offSet){
		var str = intStr + "";
		if(offSet > 0){
			while(offSet > 0){
				str = str + "0";
				offSet --;
			}
		} else if(offSet < 0){
			offSet = Math.abs(offSet);
			var strTmp = str;
			var decTmp = "";
			while(offSet > 0){
				if(strTmp.length == 0)
					strTmp = "0";
				decTmp = strTmp.substring(strTmp.length - 1, strTmp.length) + decTmp;
				if(strTmp.length <= 1)
					strTmp = "0";
				else
					strTmp = strTmp.substring(0, strTmp.length - 1);			
				offSet --;
			}
			str = strTmp + '.' + decTmp;
		}
		return str;
	};
	this.offSetNumberStrWhenSub = function(intStr, offSet){
		var str = intStr + "";
		var strTmp = str;
		var decTmp = "";
		while(offSet > 0){
			if(strTmp.length == 0)
				strTmp = "0";
			decTmp = strTmp.substring(strTmp.length - 1, strTmp.length) + decTmp;
			if(strTmp.length <= 1)
				strTmp = "0";
			else
				strTmp = strTmp.substring(0, strTmp.length - 1);			
			offSet --;
		}
		if(strTmp.length == 0)
			strTmp = "0";
		str = strTmp + '.' + decTmp;
		return str;
	};
	this.roundNum = function(number, digit) {
	    var myOldAmt = "0";
	    if(isNaN(number)) {
	  	    return roundNum(myOldAmt, digit);
	    }
	    if(isNaN(digit)) {
	        return myOldAmt;
	    }
	    var num = number;
	    var iDig = (typeof(digit) == "string") ? Number(digit) : digit;
	    num = (typeof(num) == "string") ? Number(this.removeLeadingZero(num)) : num;
	    var newNum = Math.round(num * Math.pow(10, iDig)) / (Math.pow(10, iDig) * 1.0);
	    newNum = Number(newNum).toFixed(iDig);
	    return (isNaN(newNum)) ? num : newNum;
	};
	this.removeLeadingZero = function(text) {
	    if(text) {
	        while(text.charAt(0) == '0' && text.length > 1 && text.charAt(1) != '.') {
	            text = text.substring(1);
	        }
	    }
	    return text;
	};
	num1 = this.roundNum((typeof(num1) != "number"?Number(num1):num1), 9);
	num2 = this.roundNum((typeof(num2) != "number"?Number(num2):num2), 9);
	var s1 = new String(num1);
	var s2 = new String(num2);
	var d1 = s1.indexOf(".");
	var d2 = s2.indexOf(".");
	var len = 0;
	if(d1 != -1) {
		s1 = s1.replace(/\./g,"");
		len += s1.length - d1;
	}
	if(d2 != -1) {
		s2 = s2.replace(/\./g,"");
		var len2 = s2.length - d2;
		if(type == "*") {
			len += len2;
		} else if(type == "/") {
			len -= len2;
		} else {
			if(len < len2)
				len = len2;
		}
	}
	var n1 = Number(s1);
	var n2 = Number(s2);
	var res = null;
	if(type == '+' || type == '-'){
		n1 = parseFloat(num1);
		n2 = parseFloat(num2);
	}
	if(type == "*") {
		res = new String(n1 * n2);
	} else if(type == "/") {
		res = new String(n1 / n2);
	} else if(type == '+'){
		res = new String(n1 + n2);
	} else if(type == '-'){
		res = new String(n1 - n2);
	}
	if(len == 0) {
		return Number(res);
	}
	var sign = "+";
	if(Number(res)<0) sign = "-";
	res = Math.abs(res)+"";	
	res = this.convertBigNumberStrExpress(res);

	if(type == "*") {
		res = "00000000000000000000000000000000" + res;		
		res = res.substring(0,res.length - len) + "." + res.substring(res.length - len,res.length);
		res = sign + res;
	} else if(type == "/") {
		if(res.indexOf(".") == -1) {
			res += ".00000000000000000000000000000000";
		}
		res = "00000000000000000000000000000000" + res;
		var a1 = res.split(".")[0];
		var a2 = res.split(".")[1];
		if(len < 0) {
			res = a1 + a2.substring(0,Math.abs(len)) + "." + a2.substring(Math.abs(len),a2.length);
		} else {
			res = a1.substring(0,a1.length - len) + "." + a1.substring(a1.length - len,a1.length) + a2;
		}
		res = sign + res;
	}else {
		res = sign + res;
		var multiplyValue = Math.pow(10,parseInt(len));
		return (Math.round(multiplyValue * parseFloat(res))) / multiplyValue;	
	}
	return Number(res);
};

/**
 * quickly compute the average of the number array and the return value is not accurate
 * @param support for multiple numbers or multiple objects
 * @return average
 */
Arith.quickAverage = function () {
	if (arguments.length == 0) return NaN;
	var data = arguments[0];
	for (var i = 1; i< arguments.length; i++) {
		data = this.calc(data, arguments[i], "+");
	}
	return this.div(data, arguments.length);
};

/**
 * Returns the maximum of this array of number
 * @param support for multiple numbers
 * @return maximum
 */
Arith.max = function () {
	if (arguments.length == 0) return NaN;
	var max = Number.MIN_VALUE;
	for (var i = 0; i < arguments.length; i++) {
		if (arguments[i] > max) {
			max = arguments[i];
		}
	}
	return max;
};

/**
 * Returns the minimum of this array of number
 * @param support for multiple numbers
 * @return minimum
 */
Arith.min = function () {
	if (arguments.length == 0) return NaN;
	var min = Number.MAX_VALUE;
	for (var i = 0; i < arguments.length; i++) {
		if (arguments[i] < min) {
			min = arguments[i];
		}
	}
	return min;
};