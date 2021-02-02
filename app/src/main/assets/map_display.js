var mymap = L.map('map',{
    dragging: false, // マウスドラッグによるパン操作を不可
    touchZoom: false, // タッチによるズーム操作を不可
    scrollWheelZoom: false, // スクロールによるズーム操作を不可
    doubleClickZoom: false, // ダブルクリックによるズーム操作を不可
    boxZoom: false, // [Shift] + ドラッグによるボックスズーム操作を不可
    tap: false, // タップによるズーム操作を不可
    keyboard: false, // キーボードによる操作を不可
    zoomControl: false // ズーム コントロールの非表示
});

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  maxZoom: 19,
  attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, '
 }).addTo(mymap);

 var Departure_lat;
 var Departure_lng;
 var sampleIcon;

 var LATLAT = 111263.284;
 var LNGLNG = 3600 * 25.3219892;
 var STPSTP = 6.0;

 var data = [                                       //ポケモンの追加
     {"name": "アシマリ", "src": "poke0.gif"},
     {"name": "ピカチュウ", "src": "poke1.gif"},
     {"name": "ヤドン", "src": "poke2.gif"},
     {"name": "ラプラス", "src": "poke3.gif"},
     {"name": "ロコン", "src": "poke4.gif"},
     {"name": "ブースター", "src": "poke5.gif"},
     {"name": "イーブイ", "src": "poke6.gif"},
     {"name": "モクロー", "src": "poke7.gif"},
     {"name": "ニャビー", "src": "poke8.gif"},
     {"name": "コイキング", "src": "poke9.gif"},
     {"name": "プリン", "src": "poke10.gif"},
     {"name": "ヒトカゲ", "src": "poke11.gif"},
     {"name": "フシギダネ", "src": "poke12.gif"},
     {"name": "ゼニガメ", "src": "poke13.gif"},
     {"name": "サンダース", "src": "poke14.gif"},
     {"name": "シャワーズ", "src": "poke15.gif"},
     {"name": "カビゴン", "src": "poke16.gif"},
     {"name": "ニャース", "src": "poke17.gif"},
     {"name": "カイリュー", "src": "poke18.gif"},
     {"name": "ミュウ", "src": "poke19.gif"}

 ];


mymap.on('locationfound', onLocationFound);
mymap.on('locationerror', onLocationError);

mymap.locate({setView: true, maxZoom: 19, timeout: 20000});

function onLocationFound(e) {
    Departure_lat = e.latlng.lat; //緯度y座標
    Departure_lng = e.latlng.lng; //輝度x座標


    for(let step = 0; step<6; step++){ //ポケモンの初期配置数の操作
        PokemonAppear(e);
    }


    setInterval("move()", 50);
    setInterval("PokemonAppear()", 5000); //ポケモンの出現率の操作
}

function onLocationError(e) {
    alert("現在地を取得できませんでした。" + e.message);
}

function move() {
    var theta = Android.stepDetection();
    if(theta != -10000){
        Departure_lat = Departure_lat + Math.cos(theta * Math.PI/180) * STPSTP / LATLAT;
        Departure_lng = Departure_lng - Math.sin(theta * Math.PI/180) * STPSTP / LNGLNG;

    }
    mymap.panTo( new L.LatLng( Departure_lat, Departure_lng ) ) ;

}

function PokemonAppear(e){
    var i = parseInt(Math.random() * data.length);
    /*if(i >= 18){
        i = parseInt(Math.random() * data.length);
    }*/

    var j = parseInt(Math.random() * 800) * 0.000002 - 0.0008;  //+-0.000800 ポケモン配置区分操作
    var k = parseInt(Math.random() * 600) * 0.000002 - 0.0006;  //+-0.000500

    sampleIcon = L.icon({
        iconUrl: data[i].src,
        iconRetinaUrl:data[i].src,
        iconSize:[50, 50],
        iconAnchor:[25,50],
        popupAnchor:[0, -50],
     });

    var poke_id = i;
    var poke_lat = Departure_lat + j;
    var poke_lng = Departure_lng + k;
    var mapMarker = L.marker([poke_lat, poke_lng], { icon: sampleIcon}).addTo(mymap);
    var comment = data[i].name;
    mapMarker.on( 'click', function(e) {  clickEvt(e, poke_lat, poke_lng, mapMarker, i); });
    mapMarker.bindPopup(comment).openPopup();
}

function distance(pokemonX, pokemonY){
    var distX = Math.abs(10000 * Departure_lat - 10000 * pokemonX);
    var distY = Math.abs(10000 * Departure_lng - 10000 * pokemonY);
    var dist = Math.sqrt(distX * distX + distY * distY);
    return dist;
}

function clickEvt(e, x, y, marker, i){
    if(distance(x, y) < 3.0){       //捕獲可能距離設定
        Android.ChangeActivity(i);
        mymap.removeLayer(marker); //マーカーの削除
        PokemonAppear(e);
    }else{
        alert("ポケモンとの距離が離れすぎています");
    }
}

/*function onUpdate(View v){
    Android.UpdateActivity(0);
}*/
//circle
/*var circle = L.circle([36.3, 135,5], {
	color: 'red',
	fillColor: '#f03',
	fillOpacity: 0.5,
	radius: 500
}).addTo(mymap);*/

//polygon
/*var polygon = L.polygon([
	[36.3, 135,5],
	[36.3, 135,5],
	[36.3, 135,5]
]).addTo(mymap);*/