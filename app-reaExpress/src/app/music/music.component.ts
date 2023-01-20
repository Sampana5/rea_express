import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Music } from '../shared/music';
import { map } from "rxjs/operators";
import { ItuneService } from '../test-itune/itune-service';

@Component({
  selector: 'app-music',
  templateUrl: './music.component.html',
  styleUrls: ['./music.component.css']

})

export class MusicComponent implements OnInit{
 music: Music | undefined;




  constructor(private router: Router , private route:ActivatedRoute, public ituneMusicService: ItuneService){
    this.route.params.subscribe((params => {
      if(params['musicId'])
      console.log(params['musicId']);
      this.getMusic(params['musicId']);
    }));

  }

  getMusic(musicId: string){
    this.ituneMusicService.more_info(musicId).pipe(
      map(data => {
        const res: any = data;
        console. log(res.results);
        return res. results ? res. results : []
      })

    ).subscribe((music) => this.music = music);
  }
  ngOnInit(): void {

  }

}
