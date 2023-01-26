// alert("pop up");
function Mytest(){
  let menubar = document.querySelector('#menubar');
  let mynav = document.querySelector('.navbar');
  // document.querySelector("#menubar").ClassList.toggle('fa-times')
  menubar.classList.toggle('fa-times');
  mynav.classList.toggle('active');

  // x.classList.toggle("fa-thumbs-down")
  // document.querySelector("#menubar").style.background = 'red';

}



function Smooth(){
  const bg = document.querySelector('#bgrd');
  window.addEventListener('scroll', function(){
    bg.style.backgroundSize = 50 - + window.pageXOffset + '%';
  })

}

$(window).scroll(function(){
  var scroll = $(window).scrollTop();
  $('bgrd-image').css({
    width: (50 + scroll/5) + '%'
  })
})

//  let menubar = document.querySelector('#menubar');
//   let mynav = document.querySelector('.navbar');

//   menubar.onclick = () =>{
//     menubar.classList.toggle('fa times');
//   }

  // document.querySelector('.fa fa-bars"').classList.toggle('fa times');

