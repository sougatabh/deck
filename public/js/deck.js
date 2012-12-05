function gotoAddClusterPage() {
	url = '/setup-new-host';
	gotoPage(url);
}

function loadSettingsPage() {

	url = '/show-all-settings';
	gotoPage(url);
}

function gotoPage(url) {
	window.location = url;
}


function gotoAddColumnfamilyPage(keyspace) {
	hostname = document.forms[0].hostname.value;
	clustername = document.forms[0].clustername.value;
	url = '/create-columnfamily?keyspace=' + keyspace + "&hostname=" + hostname
			+ "&clustername=" + clustername;
	gotoPage(url);
}

function gotoSearchColumnFamilyPage(keyspace, hostname, clustername) {
	url = '/search-column-family?keyspace=' + keyspace + "&hostname="
			+ hostname + "&clustername=" + clustername;
	gotoPage(url);
}

function deleteColumnfamily() {
	var e = document.forms[0].elements.length;

	for (i = 0; i < e; i++) {
		if (document.forms[0].elements[i].name == "deleteColumnFamily"
				&& document.forms[0].elements[i].checked == true) {
			deleteId = document.forms[0].elements[i].value

		}

	}

	hostname = document.forms[0].elements['hostname'].value;

	clustername = document.forms[0].elements['clustername'].value;

	selectedkeyspace = document.forms[0].elements['selectedkeyspace'].value;

	url = '/delete-columnfamily?deleteColumnFamily=' + deleteId + "&keyspace="
			+ selectedkeyspace + "&hostname=" + hostname + "&clustername="
			+ clustername

	gotoPage(url);
}

function gotoCQLEditor(hostname, clustername) {
	url = "/show-cql-editor?hostname=" + hostname + "&clustername="
			+ clustername;
	gotoPage(url);
}


function goToShowKeySpaces(hostname,clustername){
	
	url = '/show-keyspaces?hostname='+hostname+"&clustername="+clustername;
	gotoPage(url);
}

function gotoAddKeySpacePage(hostname,clustername){
	url = '/create-keyspace?hostname='+hostname+"&clustername="+clustername;
	gotoPage(url);
}

function goToShowCoumnFamilies(keyspace,hostname,clustername)
{
	url = '/show-columnfamilies?keyspace='+keyspace+"&hostname="+hostname+"&clustername="+clustername;
	gotoPage(url);
}


function gotoAddRowPage(keyspace,columnfamily){
	alert("This feature is yet to come, Please wait for next version");
	url = '/create-record?keyspace='+keyspace+'&columnfamily='+columnfamily;
}

function deleteKeySpace(hostname,clustername){
    var e= document.forms[0].elements.length;

    for(i=0;i<e;i++){
		if(document.forms[0].elements[i].name=="deleteKeyspace" && document.forms[0].elements[i].checked == true){
			deleteId = document.forms[0].elements[i].value
			
		}
	}
	
	url = '/delete-keyspace?deleteKeyspace='+deleteId+"&hostname="+hostname+"&clustername="+clustername
	gotoPage(url);		
}
    
