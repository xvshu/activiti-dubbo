/**
 * <p>Title: format</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Premium(China) Ltd.</p>
 * @author mshi
 * @version 1.0
 * @function list
 * 1.DateUtil.parse
 * 2.DateUtil.format
 * 3.DateUtil.validate
 * 4.DateUtil.compare
 * 5.DateUtil.getDaysBetweenDates
 * 6.DateUtil.equals
 * 7.DateUtil.equalsIgnoreTime
 * 8.DateUtil.add
 * 9.DecimalUtil.parse
 * 10.DecimalUtil.format
 * 11.DecimalUtil.isNaN
 * 12.DecimalUtil.formatNumber
 * 13.DecimalUtil.formatMoney
 * 14.DecimalUtil.unGroupMoney
 * 15.DecimalUtil.groupMoney
 */

/**
 * format information
 */
var FORMAT_MANAGER = {		
						'dateFormat' : "yyyy-MM-dd",
						'timeFormat' : "yyyy-MM-dd HH:mm:ss",
						'decimalSeparator' : ".",
						'groupSeparator' : ",",
						'money' : "#,##0.00",
						'money2' : "#,##0.00",
						'number' : "###0.00"
					 };
/*
 * DateUtil functions
 * These functions are used to parse, format, and manipulate Date objects.
*/
var DateUtil = {
	version : "1.0"
};

/**
 * Parses text from the beginning of the given string to produce a date.
 * For example:  DateUtil.parse("2011-11-15 18:23:21 AM", "yyyy-MM-dd HH:mm:ss a")
 *				 DateUtil.parse("2011/11/12", "yyyy/MM/dd")
 *				 DateUtil.parse("11/2011/12", "MM/yyyy/dd")
 *				 DateUtil.parse("2/11/28", "M/yy/d")
 *				 DateUtil.parse("28.2.2011", "d.M.y")
 * @param dateStr
 * @param fmtStr
 * @return date
 */
DateUtil.parse = function(dateStr, fmtStr){
	if (!dateStr) return null;
	if (!fmtStr) fmtStr = FORMAT_MANAGER.dateFormat;
	//check the value is 0 - 9.
	this.isInteger = function(val) {
		for (var i=0; i < val.length; i++) {
			if ("1234567890".indexOf(val.charAt(i)) == -1) { 
				return false; 
			}
		}
		return true;
	};
	//Gets the token from minlength to maxlength.
	this.getInt = function(str, i, minlength, maxlength) {
		for (var x = maxlength; x >= minlength; x--) {
			var token = str.substring(i, i+x);
				if (token.length < minlength) { 
					return null; 
				}
			if (this.isInteger(token)) { 
			  	return token; 
			}
		}
		return null;
	};
	var i_val = 0;
	var i_format = 0;
	var c = "";
	var token = "";
	var x = 0;
	var y = 0;
	var year = new Date().getFullYear();
	var month = 1;
	var date = 1;
	var hh = 0;
	var mm = 0;
	var ss = 0;
	var ampm = "";
  	while (i_format < fmtStr.length) {
		c = fmtStr.charAt(i_format);
		token = "";
		while ((fmtStr.charAt(i_format)==c) && (i_format < fmtStr.length)) {
			token += fmtStr.charAt(i_format++);
		}
		if (token=="yyyy" || token=="yy" || token=="y") {
			if (token=="yyyy") { 
			 	x=4;y=4; 
			}
			if (token=="yy") { 
			  	x=2;y=2; 
			}
			if (token=="y") { 
			  	x=2;y=4; 
			}
			year = this.getInt(dateStr,i_val,x,y);
			if (year == null) return null; 
			i_val += year.length;
		} else if (token=="MM" || token=="M") {
		  	month = this.getInt(dateStr, i_val, token.length, 2);
		  	if(month==null || month<1 || month>12) return null;
		  	i_val += month.length;
		} else if (token=="dd" || token=="d") {
		  	date = this.getInt(dateStr, i_val, token.length, 2);
		  	if(date==null||(date<1)||(date>31)) return null;
		  	i_val += date.length;
		} else if (token=="hh" || token=="h") {
	 	 	hh = this.getInt(dateStr, i_val, token.length, 2);
	 		if(hh==null || (hh<1) || (hh>12)) return null;
		  	i_val += hh.length;
		} else if (token=="HH" || token=="H") {
		  	hh = this.getInt(dateStr,i_val,token.length,2);
		  	if(hh==null||(hh<0)||(hh>23)) return null;
		  	i_val += hh.length;
		} else if (token=="mm" || token=="m") {
		  	mm = this.getInt(dateStr, i_val, token.length, 2);
		  	if(mm==null || (mm<0) || (mm>59)) return null;
		  	i_val += mm.length;
		} else if (token=="ss" || token=="s") {
		  	ss = this.getInt(dateStr, i_val, token.length, 2);
		  	if(ss==null || (ss<0) || (ss>59)) return null;
		  	i_val += ss.length;
		} else if (token=="a") {
		  	if (dateStr.substring(i_val, i_val+2).toLowerCase() == "am") {
		    	ampm = "AM";
		  	} else if (dateStr.substring(i_val, i_val+2).toLowerCase() == "pm") {
		    	ampm = "PM";
		  	} else {
	    		return null;
		  	}
		  	i_val += 2;
		} else {
		  	if (dateStr.substring(i_val, i_val+token.length) != token) {
		    	return null;
		  	} else {
		    	i_val += token.length;
		  	}
		}
	}
  	// validate data 
	if (i_val != dateStr.length) return null; 
	// validate month
	if (month==2) {
		if ( ((year%4==0) && (year%100 != 0)) || (year%400==0) ) {
			if (date > 29) return null; 
		} else { 
			if (date > 28) return null; 
		}
	}
	if ((month==4) || (month==6) || (month==9) || (month==11)) {
		if (date > 30) return null; 
	}
	// set ampm
	if (hh<12 && ampm == "PM") {
		hh = hh-0+12; 
	} else if (hh>11 && ampm == "AM") { 
  		hh -= 12; 
	}
	return new Date(year, month-1, date, hh, mm, ss);
};

/**
 * Formats a Date into a date/time string. 
 * For example:  DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss a")
 *				 DateUtil.format(new Date(), "yyyy/MM/dd")
 *				 DateUtil.format(new Date(), "MM/yyyy/dd")
 *				 DateUtil.format(new Date(), "M/yy/d")
 *				 DateUtil.format(new Date(), "d.M.y")
 * @param dateStr
 * @param fmtStr
 * @return string
 */
DateUtil.format = function(date, fmtStr) {
	if (!date || typeof(date) != 'object') return null;
	if (!fmtStr) fmtStr = FORMAT_MANAGER.dateFormat;
	//the lpad function pads the left-side of a string with a specific set of characters(when string1 is not null).
	this.lpad = function(str, padStr) {
		if(padStr == null) padStr = "0";
		if(str < 0 || str > 9){
			return str;
		} else {
			return padStr + str;
		}
	};
	var result = "";
	var i_format = 0;
	var c = "";
	var token = "";
	var y = date.getFullYear() + "";
	var M = date.getMonth() + 1;
	var d = date.getDate();
	var H = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	var value = {};
	value["y"] = "" + y;
	value["yyyy"] = y;
	value["yy"] = y.substring(2,4);
	value["M"] = M;
	value["MM"] = this.lpad(M);
	value["d"] = d;
	value["dd"] = this.lpad(d);
	value["H"] = H;
	value["HH"] = this.lpad(H);
	if (H == 0) {
	  value["h"] = 12;
	} else if (H > 12) {
	  value["h"] = H - 12;
	} else {
	  value["h"] = H;
	}
	value["hh"] = this.lpad(value["h"]);
	if (H > 11) { 
	  value["a"] = "PM"; 
	} else { 
	  value["a"] = "AM"; 
	}
	value["m"] = m;
	value["mm"] = this.lpad(m);
	value["s"] = s;
	value["ss"] = this.lpad(s);
	while (i_format < fmtStr.length) {
	  c = fmtStr.charAt(i_format);
	  token = "";
	  while ((fmtStr.charAt(i_format) == c) && (i_format < fmtStr.length)) {
	    token += fmtStr.charAt(i_format++);
	  }
	  if (typeof(value[token]) != "undefined") { 
	    result = result + value[token]; 
	  } else { 
	    result = result + token; 
	  }
	}
	return result;
};

/**
 * Validate date String with format string.
 * @param dateStr 
 * @param fmtStr
 * @return the value true if the date string is legal;
 *		   a value false if this Date is illegal; 
 */
DateUtil.validate = function(dateStr, fmtStr) {
	return (DateUtil.parse(dateStr, fmtStr) != null);
};

/**
 * Compares two dates for equality.
 * @param date1 string or object
 * @param date2 string or object
 * @return the value true if the argument Date is equal to this Date;
 */
DateUtil.compare = function(date1, date2) {
	if (!date1 || !date2) return null;
	if (typeof(date1) == "string"){
		date1  = this.parse(date1);
	}
	if (typeof(date2) == "string"){
		date2  = this.parse(date2);
	}
	var time1 = date1.getTime();
	var time2 = date2.getTime();
	if(time1 > time2){
		return -1;
	} else if (time1 < time2) {
		return 1;
	} else {
		return 0;
	}
};

/**
 * calculate number of days between two dates, beginDate (included) to endDate (excluded).
 * @param endDate the end date of the period. string or object
 * @param beginDate the begin date of the period. string or object
 * @return number of days from beginDate (included) to endDate (excluded)
 */
DateUtil.getDaysBetweenDates = function(beginDate, endDate) {
	if (!beginDate || !endDate) return null;
	if (typeof(beginDate) == "string"){
		beginDate  = this.parse(beginDate);
	}
	if (typeof(endDate) == "string"){
		endDate  = this.parse(endDate);
	}
	var beginTime = beginDate.getTime();
	var endTime = endDate.getTime();
	var oneDay = 24 * 60 * 60 * 1000;
	return Math.ceil((endTime - beginTime)/oneDay);
};

/**
 * Compares two dates for equality.
 * @param date1 string or object
 * @param date2 string or object
 * @return the value true if the argument Date is equal to this Date;
 */
DateUtil.equals = function(date1, date2) {
	if (!date1 || !date2) return false; 
	if (typeof(date1) == "string"){
		date1  = this.parse(date1);
	}
	if (typeof(date2) == "string"){
		date2  = this.parse(date2);
	}
	return date1.getTime() == date2.getTime();
};

/**
 * Compares two dates for equality. ignoring time considerations. 
 * @param date1 string or object
 * @param date2 string or object
 * @return the value true if the argument Date is equal to this Date;
 * @see #DateUtil.equals(date1, date2);
 */
DateUtil.equalsIgnoreTime = function(date1, date2) {
	if (!date1 || !date2) return false; 
	//Clear the date. set hour, minute, second and millisecond to 0.
	this.clearTime = function(targetDate) {
		targetDate.setHours(0);
		targetDate.setMinutes(0);
		targetDate.setSeconds(0);
		targetDate.setMilliseconds(0);
		return targetDate;
	};
	if (typeof(date1) == "string"){
		date1  = this.parse(date1);
	}
	if (typeof(date2) == "string"){
		date2  = this.parse(date2);
	}
 	return DateUtil.equals(this.clearTime(date1), this.clearTime(date2));
};

/**
 * Date Arithmetic function. Adds the specified (signed) amount of time to the given time field.
 * For example:  DateUtil.add(new Date(), 'M', -5)
 *				 DateUtil.add(new Date(), 'y', 3)
 * @param date string or object
 * @param type
 * @param interval  
 * @return date;
 */
DateUtil.add = function(date, type, interval) {
	if (!date) return null;
	if (typeof(date) == "string"){
		date  = this.parse(date);
	}
	if (!type || !interval) return date;
	interval = parseInt(interval);
	if (type == 'y') {
	  	date.setFullYear(date.getFullYear() + interval);
	} else if (type == 'M') {
  		date.setMonth(date.getMonth() + interval);
	} else if (type == 'd') {
  		date.setDate(date.getDate() + interval);
	} else if (type == 'h') {
  		date.setHours(date.getHours() + interval);
	} else if (type == 'm') {
		date.setMinutes(date.getMinutes() + interval);
	} else if (type == 's') {
  		date.setSeconds(date.getSeconds() + interval);
	}
	return date;
};

/*
 * DecimalUtil functions
 * These functions are used to format or paese decimal objects.
 */
var DecimalUtil = {
	version : "1.0"
};

/**
 * Parse a locale format data to number string.
 * For example:  DecimalUtil.parse("123,456,789.012")
 * 				 DecimalUtil.parse("123.456.789,012")
 * @param data
 * @return string
 */
DecimalUtil.parse = function(data) {
	if ( typeof(data) == "number" ) return data;
	var result = new String(data);
	result = result.replace(eval("/\\"+FORMAT_MANAGER.groupSeparator+"/g"), "");
	result = result.replace(eval("/\\"+FORMAT_MANAGER.decimalSeparator+"/g"), ".");
	return Number(result);
};

 /**
 * Formats a decimal to string, it's internal mothod.
 * For example: DecimalUtil.format("123456789.012", "money2")
 *		 		DecimalUtil.format("123456789.012", "money2", ture)
 * @param decimal must is current locale format
 * @param formatName
 * @param isGrouped default is false
 * @return string
 */
DecimalUtil.format = function(decimal, formatName, isGrouped) {
	this.localeFormat = function(data) {
		var result = new String(data);
		result = result.replace(/\,/g, "#");
		result = result.replace(/\./g, FORMAT_MANAGER.decimalSeparator);
		result = result.replace(/\#/g, FORMAT_MANAGER.groupSeparator);
		return result;
	};
	var decString;
	var fmtString;
	if (decimal) {
		decString = String(decimal).split(".");
	} else {
		decString = ['0'];
	}
	var pattern = FORMAT_MANAGER[formatName];
	if (typeof(pattern) == "undefined") pattern = FORMAT_MANAGER.money2;
	if (isGrouped) {
		pattern = pattern.replace(/\,/g,"");
	}
	if (pattern) {
		fmtString = pattern.split(".");
	} else {
		fmtString = [''];
	}
	var result = "";
	var flag = false; 
	var decStr = decString[0];
	var fmtStr = fmtString[0];
	var len = decStr.length - 1;
	for (var i=fmtStr.length - 1; i>=0 ; i--) {
		switch(fmtStr.substr(i, 1)){   
		  case '#':   
			if (len>=0) result = decStr.substr(len--, 1) + result;   
			break;   
		  case '0':   
			if (len>=0) result = decStr.substr(len--, 1) + result;   
			else result = '0' + result;   
			break;   
		  case ',' :   
			flag = true;   
			result = ',' + result;   
			break;   
		}   
	}
	if (len>=0) {   
		if (flag) {   
			var length = decStr.length;   
			for ( ; len>=0; len--){   
				result = decStr.substr(len,1) + result;   
				if( len>0 && ( (length-len)%3 ) == 0 ) result = "," + result;    
			}   
		}   
		else result = decStr.substr( 0, len+1 ) + result;   
	}
	result += ".";
	decStr = decString.length > 1 ? decString[1] : '';   
	fmtStr = fmtString.length > 1 ? fmtString[1] : '';   
	len = 0;
	for (var i=0; i<fmtStr.length ; i++) {
		switch(fmtStr.substr(i,1)){   
		  case '#':   
			if(len<decStr.length) result += decStr.substr(len++, 1);   
			break;   
		  case '0':   
			if(len<decStr.length) result += decStr.substr(len++, 1);   
			else result += '0';   
			break;   
		}   
	}
	var sign = "";
	if (result.charAt(0) == "-" || result.charAt(0) == "+" ) {
		sign = result.charAt(0);
		result = result.substring(1);
	}
	return this.localeFormat(sign + result.replace(/^\,/,"").replace(/\.$/,""));
};

/**
 * Is not a number
 * For example:  DecimalUtil.isNaN("123.456.789,012")
 * 				 DecimalUtil.isNaN("123456789,012")
 * @param data
 * @return boolean
 */
DecimalUtil.isNaN = function(data) {
	try {
		return isNaN((typeof(data) != "number"?this.parse(data):data));
	} catch (e) {
		return true;
	}
};

/*
 * Format number by digits
 * @param amtObj HTML Element
 * @param digit decimal digits
 * @param isGrouped default is false
 */
DecimalUtil.formatNumber = function(objElm, formatName, isGrouped){
	objElm.value = this.format((typeof(objElm.value) != "number"?this.parse(objElm.value):objElm.value), formatName, isGrouped);	
	return objElm.value;
};

/*
 * Format money by currency
 * @param decimal
 * @param ccy Currency string e.g. "USD"
 */
DecimalUtil.formatMoney = function(decimal, ccy){
	if (typeof(getCurrencyDecimal) == 'function') {
		var numDecimal = getCurrencyDecimal(ccy);
		return this.format((typeof(decimal) != "number"?this.parse(decimal):decimal), "money" + numDecimal);	
	} else {
		throw new Error('getCurrencyDecimal method not found.');
	}
};

/**
 * Remove group separator
 * @params: objElm HTML Element
 * @return:	objElem value
 */
DecimalUtil.unGroupMoney = function(objElm) {
	if (objElm == null) return;
	var result = objElm.value;
	objElm.value = result.replace(eval("/\\"+FORMAT_MANAGER.groupSeparator+"/g"), "");
	objElm.select();
	return objElm.value;
};

/*
 * Format money by currency
 * @param amtObj HTML Element
 * @param ccy Currency string e.g. "USD"
 */
DecimalUtil.groupMoney = function(objElm, ccy){
	if (typeof(getCurrencyDecimal) == 'function') {
		var numDecimal = getCurrencyDecimal(ccy);
		objElm.value = this.format(this.parse(objElm.value), "money" + numDecimal);	
		return objElm.value;
	} else {
		throw new Error('getCurrencyDecimal method not found.');
	}
};

/*
 * format date to string
 * format pattern:'yyyy-MM-dd'
 */
function formatColDate(fieldVal) {
	if (fieldVal) {
		return DateUtil.format(new Date(fieldVal), FORMAT_MANAGER.dateFormat);
	}
}

/*
 * format amount to string
 * format pattern:'yyyy-MM-dd hh:mm:ss'
 */
function formatColTime(fieldVal) {
	if (fieldVal) {
		return DateUtil.format(new Date(fieldVal), FORMAT_MANAGER.timeFormat);
	}
}

/*
 * format amount
 * format pattern:'#,##0.00#'
 */
function formatColMoney(fieldVal) {
	if (fieldVal) {
		return DecimalUtil.format(fieldVal);
	}
}

/*
 * format amount
 * format pattern:'###0.00#'
 */
function formatColNumber(fieldVal) {
	if (fieldVal) {
		return DecimalUtil.format(fieldVal, "number");
	}
}