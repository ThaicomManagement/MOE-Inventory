var ModalView = Backbone.View.extend({
	initialize: function(options){
		this.loadingEmployeeTemplate = Handlebars.compile($("#loadingEmployeeTemplate").html() );
		this.employeeTableTemplate = Handlebars.compile($("#employeeTableTemplate").html() );
		this.parentView = options.parentView;
	},
	events: {
		"submit form" : "onFormSubmit",
		"click input[name='employeeRadio']" : "onChangeOptionRadio",
		"click #saveBtnModal" : "onClickSaveBtn"
			
	},
	
	onClickSaveBtn: function(e) {
		if(this.mode == 'receiver') {
			$('span#receiverName').html(this.empName);
		} else if (this.mode == 'employee') {
			$('span#employeeName').html(this.empName);
		}
		
		this.parentView.empId=this.empId;
		this.parentView.empName = this.empName;
		
		this.$el.modal('hide');
	},
	
	onChangeOptionRadio: function(e) {
		//remove all tr class
    	this.$el.find('div#tableResult tr').removeClass("success");
    	
		var radioName=e.currentTarget.name;
    	var value = $("input[name='"+radioName+"']:checked").val();
    	var empName = $(e.currentTarget).parents('td').next('td').html();
    	
    	$(e.currentTarget).parents('tr').addClass('success');
    	this.empId=value;
    	this.empName=empName;
    	
    	
	},
	onFormSubmit: function(e) {
		e.preventDefault(); //otherwise a normal form submit would occur
		var name = this.$el.find('#inputName').val();
		var employees = new DomainObjectCollection();
		this.$el.find('div#tableResult').html(this.loadingEmployeeTemplate());
		
		$.when( employees.fetch({url:"/inventory/JSON/Employee", data: {name : name}, method: 'POST'}) ).done(_.bind(function(x) {
			this.$el.find('div#tableResult').html(this.employeeTableTemplate(employees.toJSON()));
			
			
		},this));
		
	},
	
	showModal: function(mode) {
		this.$el.find('div#tableResult').empty();
		this.$el.find('#inputName').val('');
		
		this.$el.modal({keyboard: false, backdrop: 'static'});
		this.mode = mode;
	}
});

var SearchView = Backbone.View.extend({
    initialize: function(options){
    	this.orgId = null;
    	this.currentRadio = null;
    	
    	this.orgSltTemplate = Handlebars.compile( $("#orgSltTemplate").html() );
    	this.loadingTemplate = Handlebars.compile($("#loadingTemplate").html() );
    	
    	$("#orgSlt").html(this.orgSltTemplate(orgs.toJSON()));
    	
    	this.fiscalYearBegin=null;
    	this.fiscalYearEnd=null;
    	
    	this.modalView = new ModalView({
			el: "#modal",
			parentView : this
			
		});
    	
    },
    
    events: {
    	"change #inputFiscalYearBegin" : "onChangeFiscalYearBegin",
    	"change #inputFiscalYearEnd" : "onChangeFiscalYearEnd",
    	
    	"click input[name='optionsRadios']" : "onChangeOptionRadio",
    	
    	"submit #searchForm" : "onSubmitSearchForm"
    		
    },
    
    
    onChangeOptionRadio: function(e) {
    	var radioName=e.currentTarget.name;
    	var value = $("input[name='"+radioName+"']:checked").val();
    	$('span.nameTxt').empty();
    	
    	if(value=="noOption") {
    		
    		this.currentRadio = null;
    		
    	} else if(value=="option1") {
    		this.currentRadio = 'employeeName';
    		this.modalView.showModal('employee');
    		
    	} else if(value=="option2") {
    		this.currentRadio = 'receiverName';
    		this.modalView.showModal('receiver');
    		
    	}
     	
    	
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
    	$('#errorModal').find('.modal-body').html(this.loadingTemplate());
    	$('#errorModal').find('.modal-header span').html("Print Dialogue");
    	$('#errorModal').modal('show');
    	
    	 $.fileDownload($("#searchForm").prop('action'), {
    	        
    	        httpMethod: "POST",
    	        data: {
    	        	orgId: this.orgId,
    	        	fiscalYearBegin: this.fiscalYearBegin,
    	        	fiscalYearEnd: this.fiscalYearEnd,
    	        	reportMode: this.currentRadio,
    	        	empId: this.empId,
    	        	empName: this.empName
    	        },
    	        successCallback: function (url) {
    	        	$('#errorModal').modal('hide');
                    
                },failCallback: function (responseHtml, url) {
                	$('#errorModal').find('.modal-header span').html("Error! Please Contact System Administrator.");
                	
                	
                	$('#errorModal').find('.modal-body').html(responseHtml);
                	$('#errorModal').modal('show');
                }
                
    	    });
    	 return false;
    	   
    }
});