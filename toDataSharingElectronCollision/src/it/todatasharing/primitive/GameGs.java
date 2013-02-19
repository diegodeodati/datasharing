package it.todatasharing.primitive;

public class GameGs {
	
	Long id;
	Long id_game_gs;
	
	
	public GameGs(){
		id = -1l;
		id_game_gs = -1l;
	}


	public GameGs(Long id, Long id_game_gs) {
		super();
		this.id = id;
		this.id_game_gs = id_game_gs;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Long getId_game_gs() {
		return id_game_gs;
	}


	public void setId_game_gs(Long id_game_gs) {
		this.id_game_gs = id_game_gs;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((id_game_gs == null) ? 0 : id_game_gs.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameGs other = (GameGs) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (id_game_gs == null) {
			if (other.id_game_gs != null)
				return false;
		} else if (!id_game_gs.equals(other.id_game_gs))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "GameGs [id=" + id + ", id_game_gs=" + id_game_gs + "]";
	}
	
	
}
