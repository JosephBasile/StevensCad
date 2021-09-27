#version 130

in vec3 surfaceNormal;
in vec3 toLightVec;

uniform vec3 lightColor;
uniform vec4 shapeColor;

void main(){
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVec = normalize(toLightVec);
    
    float dotProd = dot(unitNormal,unitLightVec);
    float brightness = max(dotProd,0.0);

    vec3 diffuse = brightness * lightColor;
    //vec4 ambient = vec4(1.3,1.3,1.3,1);
    gl_FragColor = shapeColor;//(vec4(diffuse,1) * shapeColor);
}
