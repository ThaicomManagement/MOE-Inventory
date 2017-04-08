var SearchView = Backbone.View.extend({
    initialize: function(options){
    	this.orgId = null;
    	
    	this.orgSltTemplate = Handlebars.compile( $("#orgSltTemplate").html() );
    	
    	$("#orgSlt").html(this.orgSltTemplate(orgs.toJSON()));
    	
    	this.fiscalYearBegin=null;
    	this.fiscalYearEnd=null;
    	
    },
    
    events: {
    	"change #inputFiscalYearBegin" : "onChangeFiscalYearBegin",
    	"change #inputFiscalYearEnd" : "onChangeFiscalYearEnd",
    	
    	"submit #searchForm" : "onSubmitSearchForm"
    		
    },
    onChangeFiscalYearBegin: function(e) {
    	var val = $("#inputFiscalYearBegin").val();
    	var isNum = /^\d+$/.test(val);
    	
    	
    	if(!isNum && val.length > 0) {
    		// display Error!
    		$("#inputFiscalYearBegin").parents(".form-group").addClass("has-error");
    		if($('#inputFiscalYearBeginError').length == 0) {
    			$("#inputFiscalYearBegin")
    				.parent()
    				.append("<span id='inputFiscalYearBeginError' class='help-block'>กรุณาระบุปีงบประมาณให้ถูกต้อง</span>");
    		}
    		this.fiscalYearBegin = null;
    		
    	} else if(val.length == 0){
    		// reset error
    		$("#inputFiscalYearBegin").parents(".form-group").removeClass("has-error");
    		$("#inputFiscalYearBegin").next().remove();
			
    		$("#inputFiscalYearEnd").attr("disabled", "disabled");
    		$("#inputFiscalYearEnd").val(null);
    		// reset EndFiscalYear Too
    		this.fiscalYearBegin = null;
    		this.fiscalYearEnd = null;
    		
    	} else {
    			// reset error
    		$("#inputFiscalYearBegin").parents(".form-group").removeClass("has-error");
    		$("#inputFiscalYearBegin").next().remove();
    		
    		this.fiscalYearBegin = parseInt(val);
    		
    			// enable FiscalYearEnd
    		$("#inputFiscalYearEnd").removeAttr('disabled');
    	}
    },
    
    onChangeFiscalYearEnd: function(e) {
    	var val = $("#inputFiscalYearEnd").val();
    	var isNum = /^\d+$/.test(val);
    	
    	
    	if (!isNum && val.length > 0)  {
    		// display Error!
    		$("#inputFiscalYearEnd").parents(".form-group").addClass("has-error");
    		if($('#inputFiscalYearEndError').length == 0) {
    			$("#inputFiscalYearEnd")
    				.parent()
    				.append("<span id='inputFiscalYearEndError' class='help-block'>กรุณาระบุปีงบประมาณให้ถูกต้อง</span>");
    		}
    		this.fiscalYearEnd = null;
    		
    	} else if(parseInt(val) <= this.fiscalYearBegin ){
    		// display Error!
    		$("#inputFiscalYearEnd").parents(".form-group").addClass("has-error");
    		if($('#inputFiscalYearEndError').length == 0) {
    			$("#inputFiscalYearEnd")
    				.parent()
    				.append("<span id='inputFiscalYearEndError' class='help-block'>กรุณาระบุปีงบประมาณให้มากกว่าปีงบประมาณเริ่มต้น</span>");
    		}
    		this.fiscalYearEnd = null;
    		
    	} else if(val.length == 0){
    		// reset error
    		$("#inputFiscalYearEnd").parents(".form-group").removeClass("has-error");
    		$("#inputFiscalYearEnd").next().remove();
			
    		this.fiscalYearEnd = null;
    		
    	} else {
    			// reset error
    		$("#inputFiscalYearEnd").parents(".form-group").removeClass("has-error");
    		$("#inputFiscalYearEnd").next().remove();
    		
    		this.fiscalYearEnd = parseInt(val);
    		
    		
    	}
    },
    
    onSubmitSearchForm: function(e) {
    	e.preventDefault(); //otherwise a normal form submit would occur
    	this.orgId = $("#orgSlt").val();
    	 
    	 $.fileDownload($("#searchForm").prop('action'), {
    	        
    	        httpMethod: "POST",
    	        data: {
    	        	orgId: this.orgId,
    	        	fiscalYearBegin: this.fiscalYearBegin,
    	        	fiscalYearEnd: this.fiscalYearEnd,
    	        }
    	    });
    	 return false;
    	   
    }
});