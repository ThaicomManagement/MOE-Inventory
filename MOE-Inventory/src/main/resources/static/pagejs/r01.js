var SearchView = Backbone.View.extend({
    initialize: function(options){
    	this.orgId = null;
    	
    	this.orgSltTemplate = Handlebars.compile( $("#orgSltTemplate").html() );
    	
    	$("#orgSlt").html(this.orgSltTemplate(orgs.toJSON()));
    	
    },
    
    events: {
    	"change .formSlt": "onChangeFormSlt",
    	"change .formTxt" : "onChangeFormTxt",
    	
    	"click #newFormBtn" : "onClicknewFormBtn",
    	"click #searchBtn" : "onClickSearchBtn",
    	"click #clearFormBtn" : "onClickClearFormBtn",
    	"submit #searchForm" : "onSubmitSearchForm"
    		
    },
    
    onSubmitSearchForm: function(e) {
    	e.preventDefault(); //otherwise a normal form submit would occur
    	this.orgId = $("#orgSlt").val();
    	this.fiscalYear = parseInt($("#inputFiscalYear").val());
    
    	
    	
    	 
    	 $.fileDownload($("#searchForm").prop('action'), {
    	        
    	        httpMethod: "POST",
    	        data: {
    	        	orgId: this.orgId,
    	        	fiscalYear: this.fiscalYear
    	        }
    	    });
    	 return false;
    	   
    }
});