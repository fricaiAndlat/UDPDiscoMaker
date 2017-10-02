
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<unistd.h>


int n_leds;
float *pos_leds;

char *effect_args;
int should_exit;



float *read_float_array(int len) {
    int i = 0;
    float *result = malloc(len * sizeof(float));

    scanf("%f", result);
    for(i = 1; i < len; ++i) {
        scanf (":%f", result + i);
    }
    //scanf("\r\n");

    return result;

}

void readMeta(char *line, int line_len) {
/*
    int meta_line_len;
    char *meta_line_size;
    char *meta_size;

    //read meta   init:size:args
    line = fgets(line, line_len, stdin);

    meta_line_len = strlen(line);
    meta_line_size = 1 + memchr(line, ':', line_len); //points to size:args
    effect_args = 1 + memchr(meta_line_size, ':', line_len - (meta_line_size - line)); //points to args

    meta_size = malloc(effect_args - meta_line_size);
    memcpy(meta_size, meta_line_size, effect_args - meta_line_size);
    n_leds = atoi(meta_size);
    free(meta_size);
*/
    effect_args = malloc(128);
    scanf("%d", &n_leds);
    fgets(line, line_len, stdin); //clears all \r or \n
    scanf("%s", effect_args);

    //read positions
    pos_leds = read_float_array(n_leds * 3); //3 floats per led-position
    fgets(line, line_len, stdin); //clears all \r or \n


}

void update(long time, int step){
    int i;
    fprintf(stdout, "rgb:");//sets color model to rgb. also possible is hsv.
    for(i = 0; i < n_leds; ++i){
        fprintf(stdout, "%d:%d:%d:", step % 256, step % 256, step % 256);
    }
    fprintf(stdout, "\r\n");
    fflush(stdout);
}

int main(){
    int buffer_len = 1024;
    char *buffer = malloc(buffer_len);
    int step;
    long time;
    char type;


    fprintf(stdout, "ColorEffect_v_1\r\n"); //identify as effect
    fprintf(stdout, "30\r\n");              //set fps = 30
    fflush(stdout);

    readMeta(buffer, buffer_len-1);

    while(!should_exit){
        scanf("%c", &type);

        if(type == 'q') {
            should_exit = 1;
        }else{
            scanf(":%d:%d", &time, &step);
            fgets(buffer, buffer_len, stdin); //clears all \r or \n
            update(time, step);
        }
    }

    free(buffer);

}