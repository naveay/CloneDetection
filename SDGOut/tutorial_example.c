#include <stdio.h>
#include <stdlib.h>
#include "cs_sdg.h"
#include "cs_pdg.h"
#include "cs_pdg_vertex.h"
#include "cs_pdg_vertex_set.h"
#include "cs_pdg_edge_set.h"
#include "cs_types.h"
#include "cs_utility.h"

void tutorial_example(void);
void cs_plug_main(void);
CS_DEFINE_PLUGIN(libtutorial_example);
void cs_plug_main(void)
{
    printf("---------------------\n");
    printf("C API Tutorial Output\n");
    printf("---------------------\n");

    tutorial_example();
}


/* Does all the work of the plug-in */
void tutorial_example(void)
{
    cs_pdg *allPdgs;
    cs_size_t nPdgs;
    cs_size_t size;
    cs_string proc_name;
    cs_size_t i;
    cs_size_t name_size;
    cs_result csres;
	cs_string  out_string;
	cs_pdg_vertex pdg_vertex;
	cs_pdg_vertex_set_iter pdg_vertex_set_iter;
	cs_pdg_vertex edge_pdg_vertex;
	cs_pdg_edge_kind edge_kind ;
	cs_pdg_edge_set_iter pdg_edge_set_iter;
	char filename_one[]="map_id_knnd_syntax_charname.txt";
	char filename_two[]="node.txt";
	FILE *file_one= fopen(filename_one, "w");
	FILE *file_two= fopen(filename_two, "w");
    /* Memory allocation is done by the user.  cs_sdg_pdgs() will fill
     * a user-allocated array with all the cs_pdgs in a project.
     * Calling cs_sdg_pdgs() with NULL instead of an array returns the
     * amount of space required for the array.  */
    cs_s_unload_sdg();
    csres=cs_s_read_sdg("hello.prj_files/hello.sdg",cs_true);
    csres = cs_sdg_pdgs(NULL, 0, &size);

    if( (csres != CS_SUCCESS) && (csres != CS_TRUNCATED) ) {
        printf("ERROR: %s\n", cs_resolve_error(csres));
        return;
    }

    /* Allocate the array to hold the project cs_pdgs. */
    allPdgs = (cs_pdg *)malloc( size);


    /* Fill allPdgs with the cs_pdgs in the project; write the number of
     * bytes written to allPdgs into variable "size". */
    if( cs_sdg_pdgs(allPdgs, size, &size) != CS_SUCCESS ) {
        printf("ERROR: problem getting pdgs\n");
        free(allPdgs);
        return;
    }

    /* Determine the number of cs_pdgs written to allPdgs. */
    nPdgs = size / sizeof(cs_pdg);

    /* For each cs_pdg p in allPdgs, invoke
     * cs_pdg_procedure_name(p,_,_,_). If there is a problem, print
     * error information; otherwise print the procedure name.
     */
    printf("%d Procedures defined in the project:\n", (int)nPdgs);
    csint64 idi;
    for( i = 0; i < nPdgs; i++ ) {
    	cs_pdg_vertex_set out_vertex;
    	cs_pdg_edge_set edge_set;

    	switch (csres = cs_pdg_procedure_name(allPdgs[i],NULL,0,&name_size)) {
		  case CS_SUCCESS:
		  case CS_TRUNCATED:
			  break;
		  default:
			  printf("ERROR: cs_pdg_procedure_name returned %s\n",
					 cs_resolve_error(csres));
			  continue;
		} /* switch */
		proc_name = malloc(name_size);
		switch (csres = cs_pdg_procedure_name(allPdgs[i], proc_name, name_size, &size)) {
		  case CS_SUCCESS:
			  if(proc_name[0]=='#')
				  continue;
			  fprintf(file_one,"\t%s\n",proc_name);
			  idi=cs_pdg_procedure_id(allPdgs[i]);
			  fprintf(file_one,"\tprocedureID: %lld\n",idi);
			  printf("\t%s\n",proc_name);
			  break;
		  default:
			  cs_fatal("cs_pdg_procedure_name returned %s\n",
					   cs_resolve_error(csres));
		} /* switch */
		free(proc_name);


    	//cs_pdg_find("add", &allPdgs[i]);
    	/*todo*/

    	if(cs_pdg_vertices(allPdgs[i],&out_vertex)!= CS_SUCCESS)
    		printf("ERROR: problem getting vertices\n");

    	csres=cs_pdg_vertex_set_iter_first ( out_vertex,&pdg_vertex,&pdg_vertex_set_iter);
    	cs_pdg_vertex_set_iter_close(&pdg_vertex_set_iter);

    	   for(csres=cs_pdg_vertex_set_iter_first ( out_vertex,&pdg_vertex,&pdg_vertex_set_iter);
    			(csres != CS_OUT_OF_ELEMENTS);
    			         csres=cs_pdg_vertex_set_iter_next(&pdg_vertex,&pdg_vertex_set_iter) ){

    		csint64 id1=cs_pdg_vertex_id(pdg_vertex);
    		fprintf(file_one,"\t ID:%lld\n",id1);


    		cs_pdg_vertex_kind_name(cs_pdg_vertex_kind(pdg_vertex),&out_string);
    		fprintf(file_one,"\t kind name:%s\n",out_string);
    		int type_s=cs_pdg_vertex_kind(pdg_vertex);
    		if(type_s==cs_vertex_kind_body)
    		{
    			continue;
    		}
    		cs_pdg_vertex_syntax_kind_name(cs_pdg_vertex_syntax_kind(pdg_vertex),&out_string);
    		fprintf(file_one,"\t syntax name:%s\n",out_string);

    		cs_pdg_vertex_characters(pdg_vertex,NULL,0,&name_size);
    		proc_name = malloc(name_size);
    		cs_pdg_vertex_characters(pdg_vertex,proc_name,name_size,&size);
    		fprintf(file_one,"\t char name:%s\n",proc_name);
    		free(proc_name);
    		fprintf(file_one,"\t\n");


    		cs_pdg_vertex_intra_targets(pdg_vertex,&edge_set);
    		for(csres=cs_pdg_edge_set_iter_first ( &edge_set,&edge_pdg_vertex,&edge_kind,&pdg_edge_set_iter);
    		    			(csres != CS_OUT_OF_ELEMENTS);
    		    			         csres=cs_pdg_edge_set_iter_next(&edge_pdg_vertex,&edge_kind,&pdg_edge_set_iter) ){

    						csint64 id=cs_pdg_vertex_id(edge_pdg_vertex);
    						cs_pdg ramp;
    						cs_pdg_vertex_pdg(edge_pdg_vertex , &ramp);
    						csint64 pid2=cs_pdg_procedure_id(ramp);

    						int type_d=cs_pdg_vertex_kind(edge_pdg_vertex);
							if(type_d==cs_vertex_kind_body)
							{
								continue;
							}
    			    		fprintf(file_two,"%d:%lld,%lld %d:%lld,%lld\n",type_s,idi,id1,cs_pdg_vertex_kind(edge_pdg_vertex),pid2,id);
    			    		/*
    			    		cs_pdg_vertex_kind_name(cs_pdg_vertex_kind(edge_pdg_vertex),&out_string);
    			    		printf("\t kind name:%s\n",out_string);
    			    		cs_pdg_vertex_syntax_kind_name(cs_pdg_vertex_syntax_kind(edge_pdg_vertex),&out_string);
    			    		printf("\t syntax name:%s\n",out_string);

    			    		cs_pdg_vertex_characters(edge_pdg_vertex,NULL,0,&name_size);
    			    		proc_name = malloc(name_size);
    			    		cs_pdg_vertex_characters(edge_pdg_vertex,proc_name,name_size,&size);
    			    		printf("\t char name:%s\n",proc_name);
    			    		free(proc_name);
    			    		printf("\t edge type:%d\n",edge_kind);
    			    		printf("\t\n");
    			    		*/

    		}
    		/*
    		cs_pdg_vertex_inter_targets(pdg_vertex,&edge_set);
			for(csres=cs_pdg_edge_set_iter_first ( &edge_set,&edge_pdg_vertex,&edge_kind,&pdg_edge_set_iter);
							(csres != CS_OUT_OF_ELEMENTS);
									 csres=cs_pdg_edge_set_iter_next(&edge_pdg_vertex,&edge_kind,&pdg_edge_set_iter) ){
							csint64 id=cs_pdg_vertex_id(edge_pdg_vertex);
							cs_pdg ramp;
							    						cs_pdg_vertex_pdg(edge_pdg_vertex , &ramp);
							    						csint64 pid2=cs_pdg_procedure_id(ramp);
							    			    		fprintf(file_two,"%d:%lld,%lld %d:%lld,%lld\n",type_s,idi,id1,cs_pdg_vertex_kind(edge_pdg_vertex),pid2,id);


			}
			*/
    		cs_pdg_edge_set_iter_close(&pdg_edge_set_iter);
    		fprintf(file_one,"\t------------------------\n");
    	}
    	cs_pdg_vertex_set_iter_close(&pdg_vertex_set_iter);
    }
    fclose(file_one);
    fclose(file_two);
    free(allPdgs);
}

