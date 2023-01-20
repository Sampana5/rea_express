// this.http.get(`${this.config.apiEndPoint}) = 'https://itunes.apple.com/'
import { HttpClient } from "@angular/common/http";
import { Inject, Injectable } from "@angular/core";
import { map } from "rxjs/operators";
import { AppConfig, APP_CONFIG } from "../app-config/app-config.module";
import { Music } from "../shared/music";

@Injectable()
export class ItuneService{

  public query: string | undefined;
  public music: Music[] | undefined;
  constructor(private http: HttpClient, @Inject(APP_CONFIG) private config: AppConfig) {

  }

  public searchMusic(queryTitle: string){
    this.query = queryTitle;
    this.http.get(`${this.config.apiEndPoint}search?term=${this.query}`).pipe(
      map(data => {
        const res: any = data;
        console. log(res.results);
        return res. results ? res. results : []
      })

    ).subscribe((music) => this.music = music);

  }


  public more_info(musicId: string){
    return this.http.get(`${this.config.apiEndPoint}lookup/?id=${musicId}`)
  }

  public bookfactory(item: any): Music{
    return new Music(
      item.artistName,
      item.artistviewUrl,
      item.artwork30,
      item.artworkUrl60,
      item.artworkUrl100,
      item.trackId

    )
  }
}
